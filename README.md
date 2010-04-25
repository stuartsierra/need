need: a macro to load Clojure namespaces

by Stuart Sierra, http://stuartsierra.com/

Copyright (c) Stuart Sierra, 2010. All rights reserved.  The use and
distribution terms for this software are covered by the Eclipse Public
License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file LICENSE.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.  You must not remove this notice, or any
other, from this software.




The Need for need
=================

A lot of new Clojure users have trouble remembering the correct syntax
of use/require/refer.

The `need` macro is an attempt to simplify the process of loading
Clojure namespaces.

Unfortunately, `need` is not backwards-compatible with the existing
use/require/refer syntax.  It would probably be too disruptive to
adopt this in Clojure right now, but it's an idea to think about for
the future.

I considered having `need` also replace `import` for Java classes, but
I think that loading Clojure namespaces and importing Java classes are
fundamentally different operations and should not be conflated.


Examples
--------

To load namespace without referring any symbols:

    (need clojure.contrib.io)

To load a namespace and refer two symbols:

    (need [clojure.contrib.io reader writer])

To load a namespace and refer all symbols:

    (need [clojure.contrib.io :all])

To load a namespace and refer all symbols except one:

    (need [clojure.contrib.io :exclude [spit]])

To load namespace and refer 3 symbols, renaming one of them:

    (need [clojure.contrib.io reader writer
             :rename {spit put-file}])

To load a namespace and alias it in the current namespace:

    (need [clojure.contrib.io :as io])


More
----

`need` does not support prefix lists because they are too confusing
when combined with vectors of symbols and make it hard to do
search-and-replace on namespace names.
            
`need*` is a function that behaves like `need` but evaluates its arguments.

`need` and `need*` both accept the flags `:reload`, `:reload-all`, and
`:verbose`.

See additional documentation in the docstring of the `need` macro.
