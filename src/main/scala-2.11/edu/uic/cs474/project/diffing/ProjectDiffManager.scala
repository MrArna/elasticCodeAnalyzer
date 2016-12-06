package edu.uic.cs474.project.diffing

import java.io.{File, OutputStream}

import akka.actor.{Actor, Props}
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.{DiffEntry, DiffFormatter, Edit}
import org.eclipse.jgit.lib.{ObjectId, ObjectReader, Repository}
import org.eclipse.jgit.revwalk.{RevCommit, RevTree, RevWalk}
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.{AbstractTreeIterator, CanonicalTreeParser}
import org.eclipse.jgit.util.io.DisabledOutputStream

import scala.collection.JavaConverters._


/**
  * Created by Alessandro on 30/10/16.
  */
class ProjectDiffManager extends Actor {

  override def receive: Receive = {
    case GetCommitsDiffDataMap(repoPath, newCommit) =>
      sender ! SendCommitsDiffDataMap(getDiffDataMap(repoPath, newCommit))
  }

  /**
    *
    * @param repo a jgit repo
    * @param newCommit an hash commit
    * @return the hash commit of the previous commit wrt to the passed one, in the passed repo
    */
  def getPreviousCommit(repo: Repository, newCommit: String): String = {
    val git: Git = new Git(repo)
    git.log()
      .call()
      .asScala
      .toList
      .map(rev => rev.getName)
      .sliding(2)
      .collectFirst{
        //I need the backtick ` to actually match against newCommit value
        //see http://stackoverflow.com/questions/7905023/in-scala-pattern-matching-what-is-suspicious-shadowing-by-a-variable-pattern
        //in the sliding window, the new commit is before the old one since the log order is reverse chronological
        case List(`newCommit`, previousCommit) => previousCommit}
      .getOrElse(throw new IllegalArgumentException("Commit hash not found in the local repository"))
  }

  /**
    *
    * @param path path to the repository
    * @param newCommit new commit hash
    * @return a map with (oldPath, newPath) as key and List[DiffData] as value
    */
  def getDiffDataMap(path: String, newCommit: String): Map[(String, String), List[DiffData]] = {
    val repo: Repository = getRepoFromPath(path)
    val oldCommit: String = getPreviousCommit(repo, newCommit)
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

object ProjectDiffManager {

  trait Receive
  case class GetCommitsDiffDataMap(repoPath: String, newCommit: String) extends Receive

  trait Send
  case class SendCommitsDiffDataMap(diffDataMap: Map[(String, String), List[DiffData]]) extends Send

  def props():Props = Props(new ProjectDiffManager)
}
