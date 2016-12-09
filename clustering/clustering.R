#Clear environment
rm(list=ls())

#Read dataset
dataset = read.csv("/home/andrea/Downloads/dataset.csv")

#Get number of rows and columns
nrows = nrow(dataset)
ncols = ncol(dataset)

#Get the two halves of the dataset
before = dataset[,c(1:(ncols/2))]
after = dataset[,c((ncols/2+1):ncols)]

#Remove samples with no change
diff = before - after
sums = rowSums(diff)
ids = which(sums==0)

#Get the filtered dataset
dataset_filtered = dataset[-ids,]

#Remove duplicated samples
dataset_filtered = unique(dataset_filtered)

#Define the cosine similarity
cosineSimilarity = function(a,b) 
{
  return(sum(a*b)/sqrt(sum(a^2)*sum(b^2)))
}   

#Compute lower triangle of the cosine similarity matrix
n = nrow(dataset_filtered)
D = matrix(0,n,n)

for(i in c(1:n)) {
  for(j in c(1:n)) {
    if(j<i) {
      D[i,j] = cosineSimilarity(as.numeric(dataset_filtered[i,]),as.numeric(dataset_filtered[j,]))
    }
  }
  print(i)
}

#Compute dist vector
D = as.dist(D)

#Apply hierarchical clustering
clusters = hclust(D)
plot(clusters)
