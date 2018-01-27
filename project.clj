(defproject iris-transducer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.bazaarvoice.jolt/jolt-core "0.1.0"]
                 [com.bazaarvoice.jolt/json-utils "0.1.0"]
                 [cheshire "5.8.0"]
                 [com.fasterxml.jackson.core/jackson-core "2.9.3"]
                 ]
  :main ^:skip-aot iris-transducer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
