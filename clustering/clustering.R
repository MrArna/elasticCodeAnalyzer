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
dataset_filtered2 = unique(dataset_filtered)

#Apply hierarchical clustering
clusters = hclust(dist(dataset_filtered2))
plot(clusters)