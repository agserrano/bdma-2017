# To run it:
# cd /Users/tonet/Documents/posgrado/mongo/mongodb-osx-x86_64-3.4.1/bin
# ./mongod --dbpath ../db/ &
#
# To connect:
# ./mongo

#carlos.david.herrera
#d4v3DXB1

4.1
Q1
db.restaurants.find( { "borough": "Manhattan" } )

Q2
db.restaurants.count( {
    	"borough": "Manhattan",
    	"grades.score": { $gt: 10 }
	}
)

Q3
db.restaurants.aggregate(
    {
        $unwind: "$grades"
    }, {
        $group: {
            _id: "$cuisine",
            average: { $avg: "$grades.score" }
        }
    }
)

Q4
-- lets get the coordinates
db.restaurants.find( { "name": "The Assembly Bar" } )
-- create the index
db.restaurants.createIndex({"address.coord":"2dsphere"})
-- finde the restaurants
db.restaurants.find(
   { "address.coord" :
       { $near :
          {
            $geometry : {
               type : "Point" ,
               coordinates : [ -73.876876, 40.703885 ] },
            $maxDistance : 150
          }
       }
    }
)


4.2
Q1
db.exercise2_model_1_person.aggregate([
    {
        $lookup: {
            from: "exercise2_model_1_company",
            localField: "companyEmail",
            foreignField: "email",
            as: "company"
        }
    }, {
        $unwind: "$company"
    }, {
        $project : {
            "firstName": 1,
            "company.name": 1
        }
    }
])
# { "_id" : ObjectId("58939362a6032c3588334f00"), "firstName" : "Claire", "company" : { "name" : "Porta Company" } }
# { "_id" : ObjectId("58939362a6032c3588334f01"), "firstName" : "Alex", "company" : { "name" : "Terson" } }
# { "_id" : ObjectId("58939362a6032c3588334f02"), "firstName" : "Hailey", "company" : { "name" : "Porta" } }
# { "_id" : ObjectId("58939362a6032c3588334f03"), "firstName" : "Ian", "company" : { "name" : "Buapel" } }
# { "_id" : ObjectId("58939362a6032c3588334f04"), "firstName" : "Alexandra", "company" : { "name" : "Porta" } }
# ...

Q2
db.exercise2_model_1_company.aggregate([
    {
        $lookup: {
            from: "exercise2_model_1_person",
            localField: "email",
            foreignField: "companyEmail",
            as: "employees"
        }
    },
    {
        $project : {"name": 1, "num_employees": 1, "employees.firstName": 1}
    }
])
# { "_id" : ObjectId("58939362a6032c3588334f14"), "name" : "Porta", "num_employees" : 5, "employees" : [ { "firstName" : "Hailey" }, { "firstName" : "Alexandra" }, { "firstName" : "Matthew" }, { "firstName" : "Sofia" }, { "firstName" : "Zachary" } ] }
# { "_id" : ObjectId("58939362a6032c3588334f15"), "name" : "Porta Company", "num_employees" : 3, "employees" : [ { "firstName" : "Claire" }, { "firstName" : "Michael" }, { "firstName" : "Hannah" } ] }
# { "_id" : ObjectId("58939362a6032c3588334f16"), "name" : "Furba Ltd", "num_employees" : 3, "employees" : [ { "firstName" : "Lauren" }, { "firstName" : "Logan" }, { "firstName" : "Isaac" } ] }
# { "_id" : ObjectId("58939362a6032c3588334f17"), "name" : "Terson", "num_employees" : 4, "employees" : [ { "firstName" : "Alex" }, { "firstName" : "Annabelle" }, { "firstName" : "Hailey" }, { "firstName" : "Austin" } ] }
# { "_id" : ObjectId("58939362a6032c3588334f18"), "name" : "Buapel", "num_employees" : 5, "employees" : [ { "firstName" : "Ian" }, { "firstName" : "Jaxon" }, { "firstName" : "Ella" }, { "firstName" : "Samuel" }, { "firstName" : "Samuel" } ] }

Q3
db.exercise2_model_1_company.aggregate([
    {
        $lookup: {
            from: "exercise2_model_1_person",
            localField: "email",
            foreignField: "companyEmail",
            as: "employees"
        }
    }, {
        $unwind: "$employees"
    }, {
        $group: {
            _id: "$name",
            average_age: { $avg: "$employees.age" }
        }
    }, {
        $project: {"name": 1, "average_age": 1}
    }
])

# { "_id" : "Buapel", "average_age" : 80 }
# { "_id" : "Terson", "average_age" : 44.5 }
# { "_id" : "Furba Ltd", "average_age" : 72.66666666666667 }
# { "_id" : "Porta Company", "average_age" : 26.666666666666668 }
# { "_id" : "Porta", "average_age" : 39 }

Q1
# For each person, its name and its company name
db.exercise2_model_2_company.aggregate({
        $unwind: "$employees"
    }, { $project: {"name": 1, "employees.firstName": 1} }
)

Q2
# For each company, the name and the number of employees
db.exercise2_model_2_company.find({}, { "name": 1, "num_employees": 1})

Q3
# Average age per company
db.exercise2_model_2_company.aggregate({
        $unwind: "$employees"
    }, {
        $group: {
            _id: "$name",
            average_age: { $avg: "$employees.age" }
        }
    }, { $project: {"name": 1, "average_age": 1} }
)

# The last option has created a model like this one:
# > db.exercise2_model_3_person.find().limit(1)
# { "_id" : ObjectId("5894a17fa6032c4fc9a4c332"), "firstName" : "London", "passportNumber" : "zijwfE3eS", "companyEmail" : "contact@flyhigh.biz", "companyName" : "FlyHigh", "age" : 28 }
# > db.exercise2_model_3_company.find().limit(1)
# { "_id" : ObjectId("5894a17fa6032c4fc9a4c346"), "domain" : "kleinllc.com", "email" : "company@kleinllc.com", "name" : "Klein LLC", "num_employees" : 4, "avg_age_employees" : 51 }

Q1
# For each person, its name and its company name
db.exercise2_model_3_person.find({}, {"firstName": 1, "companyName": 1})

Q2
# For each company, the name and the number of employees
db.exercise2_model_3_company.find({}, {"name": 1, "num_employees": 1})

Q3
# Average age per company
db.exercise2_model_3_company.find({}, {"name": 1, "avg_age_employees": 1})

show collections
db.exercise2_model_3_company.find().limit(1)
db.exercise2_model_3_person.find().limit(1)
db.exercise2_model_1_company.drop()




