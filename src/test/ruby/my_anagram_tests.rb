#!/usr/bin/env ruby

require 'json'
require_relative 'given_anagram_client'
require 'test/unit'

# capture ARGV before TestUnit Autorunner clobbers it

class TestCases < Test::Unit::TestCase

  # runs before each test
  def setup
    @client = GivenAnagramClient.new(ARGV)

    # add words to the dictionary
    @client.post('/words.json', nil, {"words" => ["read", "dear", "dare"]}) rescue nil
  end

  # runs after each test
  def teardown
    # delete everything
    @client.delete('/words.json') rescue nil
  end

  def test_comparing_words

    words = ["read", "dear", "dare"]

    @client.post('/words.json', nil, {"words" => words })

    # expect it not to show up in results
    res = @client.post('/anagrams/comparison.json', nil, {"words" => words }) rescue nil

    assert_equal('200', res.code, "Unexpected response code")

    body = JSON.parse(res.body)

    assert_equal(true, body['match'])
  end

  def test_comparing_words__valid_words__match

    res = @client.post('/anagrams/comparison.json', nil, {"words" => ["read", "dear", "dare"]}) rescue nil

    assert_equal('200', res.code, "Unexpected response code")

    body = JSON.parse(res.body)

    assert_equal(true, body['match'])
  end

  def test_comparing_words__unknown_word__no_match

    unknown_word = 'dare'

    @client.delete("/words/#{unknown_word}.json") rescue nil

    res = @client.post('/anagrams/comparison.json', nil, {"words" => [ unknown_word ]}) rescue nil

    assert_equal('200', res.code, "Unexpected response code")

    body = JSON.parse(res.body)

    assert_equal(false, body['match'])
  end

end
