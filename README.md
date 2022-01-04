# kisten2
Contents
1. what it is
2. how to run it
3. Naming conventions
4. A word about architecture


1. Kisten is a Jakarta EE Web Application with JSF and JPA for organizing things into buckets.
Every content has an owner, a category and a bucket. 
Owners and categories are simple entities that merely have a name, 
buckets do have a place somewhere in the flat or cellar and a filling degree.
To faster retrieve things, the contents page offers sorting contents by 
content name, bucket number, place, filling degree, category and owner.

2. The Application is developed with Jakarta EE 9.0 and GlassFish 6.1.
To run the app you need to provide some values in persistence.xml.

3. To avoid name clashes and confusion non-english names are taken for class names and properties.
E.g. "Kiste" has a property "nummer" and getNummer returns a Long and not a java.lang.Number.
Furthermore there might be a bucket datatype in some library and last but not least using other
languages is an enrichment.

4. The Architecture is three-tiered as usual:
Facelets have their backing beans, who delegate some logic to Enterprise Beans. And the EBs have
their persistence context.
IndexWrapper flatmaps Inhalt. So it can't manipulate pointers to kisten.
The Application is reactive by using Ajax for early validation.


