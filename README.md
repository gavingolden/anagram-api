# Anagram API
This provides a simple REST API that provides fast searches for anagrams with
some subset of the English language. The service maintains a mutable set
of words (a corpus) from which the anagrams are determined. Words must first be
added to the corpus before they are to be considered by the anagram
service. Only valid English words can be added.

### Prerequisites
* App: JDK
* Tests: `ruby`

### Getting Started
##### Running the app
* Clone this repository and run `./mvnw spring-boot:run`
##### Running the tests
* A few additional tests were added in a separate ruby test file along with the provided tests
* `find src/test/ruby -type f -name '*test*rb' | xargs ruby`

### Backend Design
The core optimization for identifying anagrams relies on the fact that anagrams are equal after being
lexicographically sorted. For example, the words "read" and "dear" both sort to "ader". Therefore, we can
separate each word into its respective anagram bucket immediately when it is added to the corpus by
sorting the word to find the bucket `key` for constant time lookups.

Anagram results are returned in sorted order. Buckets are `SortedSet`s so that sorting only occurs
when a bucket is modified, not for each search API call. 

### API

##### Core
- [x] `POST /words.json`: Takes a JSON array of English-language words and adds them to the corpus (data store).
- [x] `GET /anagrams/:word.json`:
  - Returns a JSON array of English-language words that are anagrams of the word passed in the URL.
  - Supports an optional query param (`limit`) that indicates the maximum number of results to return.
- [x] `DELETE /words/:word.json`: Deletes a single word from the data store.
- [x] `DELETE /words.json`: Deletes all contents of the data store.
##### Optional
- [x] Supports an optional query param (`exclude_proper_nouns`) for whether or not to include proper nouns in the list of anagrams
- [x] `POST /anagrams/comparison.json` Endpoint that takes a set of words and returns whether or not they are all anagrams of each other
- [x] `DELETE /anagrams/:word.json` Endpoint to delete a word *and all of its anagrams*

##### TODO
- [ ] Generate API documentation
- [ ] Version API endpoints
- [ ] Add metrics and transaction IDs to request logs with `MDC.Closeable`
- [ ] Kotlin provides many immutable data structures, some of which are used here. This can lead to
multiple unnecessary copies when conditionally filtering collections in stages. This can probably be
improved by using `Stream`s or mutable collections.

Some of the unimplemented endpoints would require us to efficiently track buckets by size. This could
probably be done by using an iterable `TreeMultiMap` instead of a plain unsorted `Map`. The `TreeMultiMap`
would be sorted by `bucket.size` so that the buckets can be easily iterated in order of bucket size.
- [ ] Endpoint to return all anagram groups of size >= *x*
- [ ] Endpoint that identifies words with the most anagrams 
- [ ] Endpoint that returns a count of words in the corpus and min/max/median/average word length
  - Average: This can be tracked and updated in constant time by tracking the total corpus size and recomputing
  when a word is added or removed. However, this would then require that these operations be `synchronized`
  to avoid corrupting the values due to race conditions.
  - Min/Max
    - Adding words: These can be tracked in constant time by comparing new words to the current min/max
    - Removing words: This can't be easily recomputed with the current data structure if the mix/max word is removed.
    If this was a high volume API then we could create a second corpus data structure that keeps words ordered by size. 

##### Time and Memory Complexity
`s` = word length, `b` = bucket size, `n` = corpus size
* Searching for anagrams
  - Time: `log(s) + n` for building the key, copying the bucket and removing the input word
  - Space: `b` for the cloned bucket
* Adding words to the corpus
  - Time: `log(s) + log(b)` for building the key and adding the word to a `SortedSet` bucket
  - Space: `n` because each word is stored just once
* Removing words from the corpus
  - Time: `log(s) + log(b)` for building the key and removing the word from the `SortedSet` bucket
* Clearing the corpus
  - Time: `n` for clearing each entry in the `key -> bucket` map. Reinitializing the `Map` may be
  faster by allowing the GC to reclaim everything in one fell swoop. 


### Stack
* Kotlin - I implemented this in Kotlin because I read about it when it was first released and
wanted to give it a try
* Spring Boot
* Maven
* Ruby (test)

### Final Notes 
* The data store is not thread safe because I don't think it would offer any benefit at the moment.
  
