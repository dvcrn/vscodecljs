(ns vscodecljs.core
  (:require
    [cljs.nodejs :as node]))

;; vscode module stuff
(def vscode (node/require "vscode"))
(def commands (.-commands vscode))
(def window (.-window vscode))

;; say-hello-command is the command we register as 'extension.sayHello'
;; simple test to do some things on the UI and print a message 
(defn say-hello-command []
  (.log js/console "I am the sayHello command, a function in clojurescript, not javascript")
  (.showInformationMessage window "hello world"))

;; actual callback once the extension got loaded
;; context is not used yet, needs further poking
(defn activate [context]
  (.log js/console "Test callback. I am inside core.cljs")
  
  (let [disp (.registerCommand commands "extension.sayHello" say-hello-command)]
    (.log js/console disp)))

;; for normal :simle compilation, we export "activate" to tell vscode 
;; to call this function when this extension is being loaded
(aset js/exports "activate" activate)

;; for figwheel we manually call the activate function
(def -main (fn [] (activate nil)))
(set! *main-cli-fn* -main) ;; this is required

;; figwheel callback whenever a reload happens
(defn on-js-reload []
  (.log js/console "reload"))
