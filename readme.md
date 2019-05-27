# RACK Recommender
## Code Completion Recommender - ASE19 Project
[![Build Status](https://dev.azure.com/facibal/RACKrec/_apis/build/status/acfaruk.RACKrec?branchName=master)](https://dev.azure.com/facibal/RACKrec/_build/latest?definitionId=1&branchName=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ch.uzh.rackrec%3ARACKrec&metric=alert_status)](https://sonarcloud.io/dashboard?id=ch.uzh.rackrec%3ARACKrec)
![SonarQube coverage](https://img.shields.io/sonar/http/sonarcloud.io/ch.uzh.rackrec:RACKrec/coverage.svg)

This repository is a group project at the UZH for the Advanced Software Engineering course. The goal is to create a code recommender system.

Our teams recommender is based on the following paper: https://doi.org/10.1109/SANER.2016.80

## Setup / Installation
The project consists of two maven projects in the folders example and src respectively.
The first project is the example project which runs an evaluation on the RACK recommender using the second project as a
dependency. The second project is the RACK recommender implementation usable as a library.

If you want to run the example you have to run `mvn install` on the library project, to install in your local repository,
so that the example project can access it as a dependency.

## Maven Repository
If you just want to consume the library, then you can use our package at [bintray](https://bintray.com/acfaruk/uzh/RACKrec).
The bintray repository has a big blue _set me up_ button at the top right which explains how to use the package as a client.

## Using the library

### Properties
Before using the library, you need a `Java.util.Properties` object. You can read this from a file or build it in code, 
whatever is better for you. The following properties can be set:

| Property     | Value / Explanation         | Required      |
| :---         | :---           |           ---:|
|`generate-model` | `true` or `false` (false if not set) if this is set to true, then a new file in `database-file` is generated | ✗ |
|`context-path` | `path` to folder with the context data from KaVE | if `generate-model = true` |
|`apis`| comma separated list of assembly names to generate the model from | if `generate-model = true`
|`delta`| `int` value (check paper for meaning)| ✔ | 
|`lamda`| `double` value (check paper for meaning)| ✔ |
|`database-file`| `path` to sqlite db file (generated model) | ✔ |
|`use-KKC`| `true` or `false` (false if not set) check paper for KKC definition | ✗ |

### Creating the Recommender
After you created the properties object, you can instantiate the recommender like this:
```java
//props is your property object
KaveContextModule module = new KaveContextModule(props);

//create the recommender
DefaultRecommender rec = new DefaultRecommender(module);
```

Depending on the properties set, this will create your model or just use the one provided with the database file.

### Using the Recommender
After instantiating the recommender, you can use it like this:

```java
//the recommendation needs a KaVE context to work with
Set<Pair<IMemberName, Double>> reccommenderResult = rec.query(context);
```