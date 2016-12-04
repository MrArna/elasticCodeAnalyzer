package edu.uic.cs474.project.downloading

import java.io.{File, OutputStream}
import java.lang.Iterable

import akka.actor.Actor
import edu.uic.cs474.project.{GetCommitsDiffDataMap, SendCommitsDiffDataMap}
import org.eclipse.jgit.annotations.NonNull
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.{DiffConfig, DiffEntry, DiffFormatter, Edit}
import org.eclipse.jgit.lib.{Config, ObjectId, ObjectReader, Repository}
import org.eclipse.jgit.revwalk.{FollowFilter, RevCommit, RevTree, RevWalk}
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.{AbstractTreeIterator, CanonicalTreeParser}
import org.eclipse.jgit.util.io.{DisabledOutputStream, NullOutputStream}
import sun.text.normalizer.Replaceable

import scala.collection.JavaConverters._
import scala.sys.process.Process


/**
  * Created by Alessandro on 30/10/16.
  */
class ProjectVersionManager extends Actor {

  override def receive: Receive = {
    case GetCommitsDiffDataMap(repoPath, oldCommit, newCommit) =>
      sender ! SendCommitsDiffDataMap(getDiffDataList(repoPath, oldCommit, newCommit))
  }

  /**
    *
    * @param path path to the repository
    * @param oldCommit old commit hash
    * @param newCommit new commit hash
    * @return a map with (oldPath, newPath) as key and List[DiffData] as value
    */
  def getDiffDataList(path: String, oldCommit: String, newCommit: String): Map[(String, String), List[DiffData]] = {
    val repo: Repository = getRepoFromPath(path)
    val formatter: DiffFormatter = customDiffFormatter(repo)
    val diffData = getDiffEntries(formatter, repo, oldCommit, newCommit)
      .map(diffEntry =>
        //key for the edit map made by tuple (oldPath, newPath), since a file can be renamed
        ((diffEntry.getOldPath, diffEntry.getNewPath),
          //value for the map, a list of diffdata (with empty edits removed)
          formatter
            .toFileHeader(diffEntry)
            .toEditList
            .asScala
            .toList
            .filterNot(edit => edit.getType == Edit.Type.EMPTY)
            .map(edit => new DiffData(DiffData.matchDiffDataType(edit.getType),
              edit.getBeginA + 1,
              edit.getBeginB + 1,
              edit.getEndA + 1,
              edit.getEndB + 1))))
        .toMap
    formatter.close()
    diffData
  }

  /**
    *
    * @param formatter a jgit diffformatter
    * @param repo a jgit repo
    * @param oldCommit old commit hash
    * @param newCommit new commit hash
    * @return a list of diffentry, each per modified file
    */
  def getDiffEntries(formatter: DiffFormatter, repo: Repository, oldCommit: String, newCommit: String): List[DiffEntry] = {
    customDiffFormatter(repo).scan(prepareTreeParser(repo, oldCommit),
      prepareTreeParser(repo, newCommit)).asScala.toList
  }

  /**
    *
    * @param repo jgit repository
    * @return an open jgit diffformatter for performing comparisons
    */
  def customDiffFormatter(repo: Repository): DiffFormatter = {
    val disabledOutputStream : OutputStream  = DisabledOutputStream.INSTANCE
    val formatter: DiffFormatter = new DiffFormatter(disabledOutputStream)
    formatter.setDetectRenames(true)
    formatter.setRepository(repo)
    formatter
  }

  /**
    *
    * @param path full path to the repository
    * @return a jgit repository object
    */
  def getRepoFromPath(path: String): Repository = {
    var builder: FileRepositoryBuilder = new FileRepositoryBuilder()
    val file: File = new File(path)
    builder.setGitDir(file)
      .readEnvironment()
      .findGitDir()
      .build()
  }

  /**
    *
    * @param repo a jgit repository
    * @param objectId the commit hash whose tree has to be parsed
    * @return a jgit tree parser for the specified commit
    */
  def prepareTreeParser(repo: Repository, objectId: String): AbstractTreeIterator = {
    val walk: RevWalk = new RevWalk(repo)
    val commit: RevCommit = walk.parseCommit(ObjectId.fromString(objectId))
    val tree: RevTree = walk.parseTree(commit.getTree.getId)
    val treeParser: CanonicalTreeParser = new CanonicalTreeParser()
    val reader: ObjectReader = repo.newObjectReader()
    treeParser.reset(reader, tree.getId)
    walk.dispose()
    treeParser
  }
}
