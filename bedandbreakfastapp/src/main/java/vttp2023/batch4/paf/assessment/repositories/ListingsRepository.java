package vttp2023.batch4.paf.assessment.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 * db.listings.aggregate([
			{
				$match : {
					"address.suburb" : {
						$ne : ""
					}
				}
			},
			{
				$project: {
					_id: "$address.suburb"
				}
			}
		]).toArray();
	 *
	 */
	public List<String> getSuburbs(String country) {
		MatchOperation matchOps = Aggregation.match(Criteria.where("address.suburb").ne(""));
		ProjectionOperation projOps = Aggregation.project("_id").and("$address.suburb").as("_id");
		Aggregation pipeline = Aggregation.newAggregation(matchOps, projOps);
		AggregationResults<Document> results = template.aggregate(pipeline, "listings", Document.class);
		return results.getMappedResults().stream().map(doc -> doc.getString("_id")).toList();
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *db.listings.aggregate([
			{
				$match: {
					$and : [
						{"address.suburb" : {$regex : "Lilyfield/Rozelle" , $options: "i"}},
						{price: {$lte : 500.00}},
						{accommodates: {$gte : 1}},
						{min_nights: {$lte: 5}},
					]
				}
			},
			{
				$project : {
					_id : 1,
					name : 1,
					accommodates: 1,
					price: 1
				}
			},
			{
				$sort: {price: -1}
			}
		])
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		MatchOperation matchOps = Aggregation.match(
			Criteria.where("address.suburb").regex(suburb, "i")
						.and("price").lte(priceRange)
						.and("accommodates").gte(persons)
						.and("min_nights").lte(duration)
			);
			ProjectionOperation projOps = Aggregation.project("_id", "name", "accommodates", "price");
			SortOperation sortOps = Aggregation.sort(Sort.by(Direction.DESC, "price"));
			Aggregation pipeline = Aggregation.newAggregation(matchOps, projOps, sortOps);
			AggregationResults<Document> results = template.aggregate(pipeline, "listings", Document.class);
			List<Document> docs = results.getMappedResults();
			List<AccommodationSummary> accoms = docs.stream().map(doc -> {
				AccommodationSummary summary = new AccommodationSummary();
				summary.setAccomodates(doc.getInteger("accommodates"));
				summary.setId(doc.getString("_id"));
				summary.setName(doc.getString("name"));
				summary.setPrice(doc.get("price", Number.class).floatValue());
				return summary;
			}).toList();
		return accoms;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
