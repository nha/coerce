# coerce

The project uses [Midje](https://github.com/marick/Midje/).

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

# Specification

te a function that coerces an object — originating from a parsed JSON string — according to a schema. The function should be generic and be able to coerce an object of arbitrary depth and composition. The following Javascript types should be supported:

    Number
    Date
    Boolean
    String

Below is an example schema definition, that should successfully coerce an object of a corresponding structure, with all values as strings. Keys that do not exist in the schema definition should be thrown away from the coerced object.

(def schema {
    :_id  Double
    :name String
    :url  String
    :date DateTime
    :meta {
        :image-url String
        :content   String
        :extern {
            :sample Boolean
            :frame  Boolean
            :id     Double
            }}
        :order [{
            :language String
            :price    Double
            }]
        :active Boolean
        :i18n [{
            :name String
            :description String
            }]}


