Java Regular Expressions
======================


###Brackets
Brackets are used to find a range of characters:

    Expression 	Description
    [abc] 	Find one character from the options between the brackets
    [^abc] 	Find one character NOT between the brackets
    [0-9] 	Find one character from the range 0 to 9

###Metacharacters
Metacharacter are characters with a special meaning:

    Metacharacter 	Description
    | 	Find a match for any one of the patterns separated by | as in: cat|dog|fish
    . 	Find just one instance of any character
    ^ 	Finds a match as the beginning of a string as in: ^Hello
    $ 	Finds a match at the end of the string as in: World$
    \d 	Find a digit
    \s 	Find a whitespace character
    \b 	Find a match at the beginning of a word like this: \bWORD, or at the end of a word like this: WORD\b
    
    
###Quantifiers
Quantifiers define quantities:
    
    Quantifier 	Description
    n+ 	Matches any string that contains at least one n
    n* 	Matches any string that contains zero or more occurrences of n
    n? 	Matches any string that contains zero or one occurrences of n
    n{x} 	Matches any string that contains a sequence of X n's
    n{x,y} 	Matches any string that contains a sequence of X to Y n's
    n{x,} 	Matches any string that contains a sequence of at least X n's

###Escape
- If your expression needs to search for one of the special characters you can use a backslash ( \ ) to escape them.
- In Java, backslashes in strings need to be escaped themselves, so two backslashes are needed to escape special characters.
- For example, to search for one or more question marks you can use the following expression: "\\?"
 
###Syntax
- Here is the table listing down all the regular expression metacharacter syntax available in Java −
    
    
    Subexpression 	Matches
    ^ 	Matches the beginning of the line.
    $ 	Matches the end of the line.
    . 	Matches any single character except newline. Using m option allows it to match the newline as well.
    [...] 	Matches any single character in brackets.
    [^...] 	Matches any single character not in brackets.
    \A 	Beginning of the entire string.
    \z 	End of the entire string.
    \Z 	End of the entire string except allowable final line terminator.
    re* 	Matches 0 or more occurrences of the preceding expression.
    re+ 	Matches 1 or more of the previous thing.
    re? 	Matches 0 or 1 occurrence of the preceding expression.
    re{ n} 	Matches exactly n number of occurrences of the preceding expression.
    re{ n,} 	Matches n or more occurrences of the preceding expression.
    re{ n, m} 	Matches at least n and at most m occurrences of the preceding expression.
    a| b 	Matches either a or b.
    (re) 	Groups regular expressions and remembers the matched text.
    (?: re) 	Groups regular expressions without remembering the matched text.
    (?> re) 	Matches the independent pattern without backtracking.
    \w 	Matches the word characters.
    \W 	Matches the nonword characters.
    \s 	Matches the whitespace. Equivalent to [\t\n\r\f].
    \S 	Matches the nonwhitespace.
    \d 	Matches the digits. Equivalent to [0-9].
    \D 	Matches the nondigits.
    \A 	Matches the beginning of the string.
    \Z 	Matches the end of the string. If a newline exists, it matches just before newline.
    \z 	Matches the end of the string.
    \G 	Matches the point where the last match finished.
    \n 	Back-reference to capture group number "n".
    \b 	Matches the word boundaries when outside the brackets. Matches the backspace (0x08) when inside the brackets.
    \B 	Matches the nonword boundaries.
    \n, \t, etc. 	Matches newlines, carriage returns, tabs, etc.
    \Q 	Escape (quote) all characters up to \E.
    \E 	Ends quoting begun with \Q.
    
    
 ##Classes
   
    Regex Character classes
    No.	Character Class	Description
    1	[abc]	a, b, or c (simple class)
    2	[^abc]	Any character except a, b, or c (negation)
    3	[a-zA-Z]	a through z or A through Z, inclusive (range)
    4	[a-d[m-p]]	a through d, or m through p: [a-dm-p] (union)
    5	[a-z&&[def]]	d, e, or f (intersection)
    6	[a-z&&[^bc]]	a through z, except for b and c: [ad-z] (subtraction)
    7	[a-z&&[^m-p]]	a through z, and not m through p: [a-lq-z](subtraction)
    
##Link
- Tutorial [vogella](https://www.vogella.com/tutorials/JavaRegularExpressions/article.html)
- Java Regular Expressions [w3schools](https://www.w3schools.com/java/java_regex.asp)
