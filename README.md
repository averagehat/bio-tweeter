#bio-tweeter

A fork of https://github.com/danmidwood/clojure-questions for biostars.

This is a tiny little twitter bot that uses the ~~StackOverflow API~~ biostars.org (maybe seqanswers in the future?) to tweet new questionsfrom the site.

The bot is running under the name
[@QuestionsBio](https://twitter.com/QuestionsBio)

## Usage

The easiest way to do get keys is to create a new twitter account specifically for the bot, then register a new application for the bot, and then generate user tokens for that application. That'll give you everything you need.

For development you can use [environ](https://github.com/weavejester/environ) to manage the tokens, drop all of these keys into your leiningen profile (~/.lein/profiles.clj). It should look like this:

```clojure
{:user
 {:env {:clojureqs-app-consumer-key ""
        :clojureqs-app-consumer-secret ""
        :clojureqs-user-access-token ""
        :clojureqs-user-access-token-secret "" }}}
```

Then you're ready to go. 

## License

Copyright © 2012, 2014 Anthony Grimes, Dan Midwood
2015 Mike Panciera

Distributed under the Eclipse Public License, the same as Clojure.
