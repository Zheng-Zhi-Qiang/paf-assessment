package vttp2023.batch4.paf.assessment.services;

import java.io.StringReader;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class ForexService {

	private String api = "https://api.frankfurter.app/latest";
	private RestTemplate template = new RestTemplate();

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {
		String url = UriComponentsBuilder.fromUriString(api)
                        .queryParam("from", from.toUpperCase())
                        .queryParam("to", to.toUpperCase())
                        .toUriString();
		RequestEntity<Void> req = RequestEntity.get(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .build();
		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);
			JsonObject data = Json.createReader(new StringReader(resp.getBody())).readObject();
			Float exchangeRate = Float.parseFloat(data.getJsonObject("rates").get(to.toUpperCase()).toString());
			return amount * exchangeRate;
		}
		catch (Exception e){
			return -1000f;
		}
		
		
	}
}
