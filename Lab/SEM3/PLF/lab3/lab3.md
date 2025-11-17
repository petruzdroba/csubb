We don’t add a cut (`!`) because we want Prolog to backtrack and explore **all possible valid sequences**, not stop at the first solution.

Prolog explores all solutions using **backtracking**: it tries the first available choice, goes as deep as possible,
and when it reaches a dead end or outputs a solution, it automatically returns to the 
previous choice point to try the next alternative.