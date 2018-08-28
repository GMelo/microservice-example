package org.gmelo.hsbc.assignment.analytics.service;

import com.mongodb.spark.MongoSpark;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class SparkQueryServiceIT {

    @Test
    public void testConnect() {
        Logger.getLogger("org").setLevel(Level.WARN);

        SparkSession spark = SparkSession.builder()
                .master("local[*]")
                .appName("MongoSparkConnectorIntro")
                .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/hsbc_assignment.CreditCardTransaction")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.myCollection")
                .getOrCreate();

        String filter = "{ $match:{ ts :{$gte:ISODate('%s'),$lte:ISODate('%s')}}}";
        Timestamp tsg = new Timestamp(System.currentTimeMillis());
        Timestamp tsl = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        Dataset<Row> implicitDS = MongoSpark
                .load(jsc)
             //   .withPipeline(singletonList(Document.parse(String.format(filter,tsl.toString(),tsg.toString()))))
               // .toDS(InternalTransactional.class);
                .toDF();
//                .drop("_class")
//                .drop("_id");

        implicitDS.createOrReplaceTempView("CreditCardTransaction");
        implicitDS.printSchema();
        implicitDS.show();
        Dataset<Row> centenarians = spark.sql("SELECT * FROM CreditCardTransaction where ts > '2018-08-18' ");

        List<String> map = centenarians.toJSON().collectAsList();

        System.out.println(map);
    }

}