# Run instructions :
# ./bin/spark-submit --master "local[2]"  \
#                    --conf "spark.mongodb.input.uri=mongodb://127.0.0.1/hsbc_assignment.CreditCardTransaction?readPreference=primaryPreferred" \
#                    --conf "spark.mongodb.output.uri=mongodb://127.0.0.1/hsbc_assignment.CreditCardTransaction" \
#                    --packages org.mongodb.spark:mongo-spark-connector_2.11:2.3.0\
#                    analytics.py

from pyspark.sql import SparkSession

spark = SparkSession.builder.appName("HSBC Assignment Analytics").getOrCreate()

#Make the logs less chatty
logger = spark._jvm.org.apache.log4j
logger.LogManager.getLogger("org"). setLevel( logger.Level.ERROR )

#if the dataset gets large we can start to optimise by filtering the data on the way in
# with a pipeline filter.
df = spark.read.format("com.mongodb.spark.sql").load().drop("_class").drop("_id")
# in real life we would get the date from the driving script
grouped = df.filter(df["ts"] < "2018-08-18 21:10").groupBy("firstName").sum("transactionValue")

json = grouped.toJSON()
# write json to a rest endpoint to