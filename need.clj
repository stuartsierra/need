(ns need)

(defn- need-refer [libspec flags verbose?]
  (let [lib (first libspec)
        args (rest libspec)]
    (when verbose? (prn (list* 'require lib flags)))
    (apply require lib flags)
    (loop [args args
           syms (list)
           renames nil
           excludes nil
           all? false
           alias-name nil]
      (if (seq args)
        (let [arg (first args)]
          (cond (symbol? arg)
                (recur (next args) (conj syms arg) renames excludes all? alias-name)
            
                (= :as arg)
                (recur (nnext args) syms renames excludes all? (second args))
                

                (= :rename arg)
                (recur (nnext args) (concat syms (keys (second args)))
                       (second args) excludes all? alias-name)

                (= :all arg)
                (recur (next args) syms renames excludes true alias-name)

                (= :exclude arg)
                (recur (nnext args) syms renames (second args) true alias-name)

                :else
                (throw (IllegalArgumentException.
                        (str "Bad option in 'need' vector:" (pr-str arg))))))
        (do (if all?
              (do (when verbose?
                    (prn (list 'refer lib :rename renames :exclude excludes)))
                  (refer lib :rename renames :exclude excludes))
              (do (when verbose?
                    (prn (list 'refer lib :only syms :rename renames)))
                  (refer lib :only syms :rename renames)))
            (when alias-name
              (when verbose?
                (prn (list 'alias alias-name lib)))
              (alias alias-name lib)))))))

(defn need*
  "Like `need` but evaluates all its arguments."
  [& args]
  (let [flags (filter keyword? args)
        libs (remove keyword? args)
        verbose? (some #{:verbose} flags)]
    (when verbose? (prn (list* 'need* args)))
    (doseq [lib libs]
      (cond (symbol? lib) (do (when verbose? (prn (list* 'require lib flags)))
                              (apply require lib flags))
            (vector? lib) (need-refer lib flags verbose?)
            :else (throw (IllegalArgumentException.
                          "Arguments to need* must be symbols or vectors."))))))

(defmacro need
  "Replacement for require/use/refer.

  Arguments are symbols, vectors, or flags, and are not evaluated.

  Valid flags are :reload, :reload-all, and :verbose.

  Symbol arguments are namespace names.  Loads the namespace like
  `require`.

  Vector arguments are [namespace-name & symbols-or-options].  Loads
  the named namespace and refers the named symbols into the current
  namespace.

  Valid options within a vector are:
    :all 
      to refer all symbols.

    :exclude [& symbols] 
       to refer all symbols except those named.

    :rename {old-name new-name} 
       to refer symbols as different names.  May be combined with :all
       or :exclude.

    :as alias-name
       to create an alias for the namespace name.
"
  [& args]
  `(apply need* '~args))
