## This program uses cache values for quick computation of inverse of matrix.
## The whole idea is to avoid unnecessary computation if the inverse of the matrix
## is already present in cache.

## The makeCacheMatrix stores the matrix and its inverse in cache.It can also be used to 
## set and get the matrix and its inverse values.

makeCacheMatrix <- function(x = matrix()) {
  i <- NULL
  set <- function(y) {
    x <<- y
    i <<- NULL
  }
  get <- function() x
  setinverse <- function(inv) i <<- inv
  getinverse <- function() i
  list(set = set, get = get,
       setinverse = setinverse,
       getinverse = getinverse)
}


## The cacheSolve() functions takes in makeCacheMatrix object as an argument.
## It searches for the inverse of the matrix in cache.If it present it returns 
## the result directly.If not, it computes the inverse and also updates the 
## makeCacheMatrix object with its inverse.

cacheSolve <- function(x) {
  ## Return a matrix that is the inverse of 'x'
  i <- x$getinverse()
  if(!is.null(i)) {
    message("Getting cached data")
    return(i)
  }
  data <- x$get()
  i <- solve(data)
  x$setinverse(i)
  i
  
}
