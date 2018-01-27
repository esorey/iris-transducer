(ns iris-transducer.core
  (:gen-class)
  (:require [cheshire.core :as json])
  (:import (com.bazaarvoice.jolt Chainr JsonUtils)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(comment
  (def chainrSpecJSON (JsonUtils/filepathToList "json/sample/spec.json"))
  (def chainr (Chainr/fromSpec chainrSpecJSON))
  (def inputJSON (JsonUtils/filepathToObject "json/sample/input.json"))
  (def transformedOutput (.transform chainr inputJSON))
  (JsonUtils/toJsonString transformedOutput)
  (type inputJSON)
  ;; => {"rating" {"primary" {"value" 3}, "quality" {"value" 3}}}
  transformedOutput
  ;; => {"Rating" 3, "SecondaryRatings" {"quality" {"Id" "quality", "Value" 3, "Range" 5}}, "Range" 5}

  ;; read a spec from json file
  (defn read-spec-from-file [f]
    (-> f
        JsonUtils/filepathToList))

  (read-spec-from-file "json/sample/spec.json")

  ;; build a chainr from json file
  (defn file->chainr [f]
    (-> f
        JsonUtils/filepathToList
        Chainr/fromSpec))

  (file->chainr "json/sample/spec.json")

  ;; build a chainr from clojure map
  (def zzf "json/sample/spec.json")

  (-> zzf
      clojure.java.io/reader
      json/parse-stream
      Chainr/fromSpec
      )

  )



;;;;;; EXAMPLE TRANSFORMATION BELOW ;;;;;;

;; sample input
;; Note that I have to cast internal vectors to java.util.ArrayList
(def asset
  {"id"       "xxxxxxxxxxxxxxxxxxxxxxxx",
   "keywords"  (java.util.ArrayList. ["voyage", "potato"]),
   "credits"   (java.util.ArrayList. [{"kind"  "actor",
                                       "name"  "Jimmy Fallon"},
                                      {"kind"  "actor",
                                       "name"  "Nicholas Cage"},
                                      {"kind"  "director",
                                       "name"  "M. Night Shyamalan"}])
   })


;; target output
;; {"ID" : "xxxxxxxxxxxxxxxxxxxxxxxx"
;; "kws" : ["voyage", "potato", "Jimmy Fallon", "Nicholas Cage","M. Night Shyamalan"]
;; }


;; The spec defining the transformation
(def spec
  [{"operation" "shift",
    "spec" {
            "id" "ID",
            "keywords" "kws",
            "credits" {
                       "*" {
                            "name" "kws",}}
            }
    }])

;; A Chainr object encodes a transformation scheme
(def chainr (-> spec (Chainr/fromSpec)))

;; Run the transform
#_(.transform chainr asset)
;; => {"ID" "xxxxxxxxxxxxxxxxxxxxxxxx", "kws" ["voyage" "potato" "Jimmy Fallon" "Nicholas Cage" "M. Night Shyamalan" "Jimmy Fallon" "Nicholas Cage" "M. Night Shyamalan" "Jimmy Fallon" "Nicholas Cage" "M. Night Shyamalan"]};; => {"ID" "xxxxxxxxxxxxxxxxxxxxxxxx", "kws" ["voyage" "potato" "Jimmy Fallon" "Nicholas Cage" "M. Night Shyamalan" "Jimmy Fallon" "Nicholas Cage" "M. Night Shyamalan" "Jimmy Fallon" "Nicholas Cage" "M. Night Shyamalan"]}
;; Yayyyyyy
