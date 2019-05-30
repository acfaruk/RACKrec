# RACK Recommender Evaluation
This part of the repository uses and evaluates the recommender with a model of choice. A context from a completion event from the KaVe events dataset is fed into the recommender and compaired against the proposals from the same completion event.


## Configuring Evaluation.java
All files used in this evaluation have to be put into the */target/classes/* directory. This includes a model/database file and an *Events/* folder where .zip files containing completion events are located.

As described in the readme of recommender, properties have to be set. To use the recommender when a model has already been built, the "database-file" property has to be set to an already existing file. In our case we provide 2 different database files, a "simple" and an "extended" file.
##### Simple Database
This database does not include constructors (.ctor) in its recommendations, nor does it provide the API name of the proposed method.
##### Extended Database
This database does include constructors (.ctor), as well as the API name of the proposed method.

The "generate-model" property should be set to false, as this evaluation does not generate a new model. To use the KAC, as well as the KKC approach the "use-KKC" property should be set to true. This however, has a worse performance compared to only using the KAC approach.

Next, the Event Data Location has to be set, by default this is, as mentioned above, the *Events/* directory.

The last string that has to be set correctly, is the databaseType variable. It **has** to match the type of database is used for the recommender, as the evaluation works differently, depending on database type. Currently the values "simple" and "extended" are supported.

With the configuration done, the Evaluation.java can be run and at the end a graph displaying the mean metrics of all recommender results is drawn.
