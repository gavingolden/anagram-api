# Anagram API
This provides a simple REST API that provides fast searches for anagrams with
some subset of the English language. The service maintains a mutable set
of words (a corpus) from which the anagrams are determined. Words must be
first added to the corpus before they are to be considered by the anagram
service.

### Prerequisites
* `ruby` must be installed to run the tests

### Getting Started
##### Running the app
* Clone this repository and run `./mvnw spring-boot:run`
##### Running the tests
* `find src/test/ruby -type f -name '*test*rb' | xargs ruby`

### API
##### Core
- [x] `POST /words.json`: Takes a JSON array of English-language words and adds them to the corpus (data store).
- [x] `GET /anagrams/:word.json`:
  - Returns a JSON array of English-language words that are anagrams of the word passed in the URL.
  - Supports an optional query param that indicates the maximum number of results to return.
- [x] `DELETE /words/:word.json`: Deletes a single word from the data store.
- [x] `DELETE /words.json`: Deletes all contents of the data store.
##### Optional
- [ ] Endpoint that returns a count of words in the corpus and min/max/median/average word length
- [x] Respect a query param for whether or not to include proper nouns in the list of anagrams
- [ ] Endpoint that identifies words with the most anagrams
- [x] Endpoint that takes a set of words and returns whether or not they are all anagrams of each other
- [ ] Endpoint to return all anagram groups of size >= *x*
- [x] Endpoint to delete a word *and all of its anagrams*

### Stack
* Kotlin
* Spring Boot
* Maven
* Ruby (test)