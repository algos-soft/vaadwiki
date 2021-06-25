##JSON and BSON
JSON and BSON are close cousins, as their nearly identical names imply, but you wouldn’t know it by looking at them side-by-side. JSON, or JavaScript Object Notation, is the wildly popular standard for data interchange on the web, on which BSON (Binary JSON) is based. 

####JSON
JavaScript Object Notation, more commonly known as JSON, was defined as part of the JavaScript language in the early 2000s by JavaScript creator Douglas Crockford, though it wasn’t until 2013 that the format was officially specified.

JavaScript objects are simple associative containers, wherein a string key is mapped to a value (which can be a number, string, function, or even another object). This simple language trait allowed JavaScript objects to be represented remarkably simply in text.

By virtue of being both human- and machine-readable, and comparatively simple to implement support for in other languages, JSON quickly moved beyond the web page, and into software everywhere.

JSON quickly overtook XML, is more difficult for a human to read, significantly more verbose, and less ideally suited to representing object structures used in modern programming languages.

- JSON is a text-based format, and text parsing is very slow
- JSON’s readable format is far from space-efficient, another database concern
- JSON only supports a limited number of basic data types

####BSON
BSON simply stands for “Binary JSON,” and that’s exactly what it was invented to be. BSON’s binary structure encodes type and length information, which allows it to be parsed much more quickly.

Since its initial formulation, BSON has been extended to add some optional non-JSON-native data types, like dates and binary data, without which MongoDB would have been missing some valuable support.

MongoDB stores data in BSON format both internally, and over the network.

Unlike systems that simply store JSON as string-encoded values, or binary-encoded blobs, MongoDB uses BSON to offer the industry’s most powerful indexing and querying features on top of the web’s most usable data format.

Firstly, BSON objects may contain Date or Binary objects that are not natively representable in pure JSON. 

####Schema Flexibility
One of the big attractions for developers using databases with JSON and BSON data models is the dynamic and flexible schema they provide when compared to the rigid, tabular data models used by relational databases.

1) Firstly, JSON documents are polymorphic – fields can vary from document to document within a single collection (analogous to table in a relational database).
2) Secondly, there is no need to declare the structure of documents to the database – documents are self-describing. Developers can start writing code and persist objects as they are created.
3) Thirdly, if a new field needs to be added to a document, it can be created without affecting all other documents in the collection, without updating a central system catalog and without taking the database offline.


###Gson
Google Gson is an open source, Java-based library developed by Google. It facilitates serialization of Java objects to JSON and vice versa. This tutorial adopts a simple and intuitive way to describe the basic-to-advanced concepts of Google Gson and how to use its APIs.


##Link
- [JSON and BSON](https://www.mongodb.com/json-and-bson/)
- [JSON e BSON](http://university.mongodbitalia.it/json-e-bson)
- [Advantages of MongoDB](https://www.mongodb.com/advantages-of-mongodb/)
- [Google Gson Tutorial](https://www.tutorialspoint.com/gson/index.htm)
- [Gson - Object Data Binding](https://www.tutorialspoint.com/gson/gson_object_data_binding.htm)
- [Gson - Serializing Inner Classes](https://www.tutorialspoint.com/gson/gson_inner_classes.htm)
