package com.n26.demo.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.n26.demo.entity.TransactionDto;
import com.n26.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transactions")
public class TransactionsResource {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity postTransation(String jsonInString) {

        TransactionDto transactionDto = null;

        try {
            // check if json is valid. The following line will cause an exception if the json is not valid
            JsonElement jsonElement = new JsonParser().parse(jsonInString).getAsJsonObject();

            // check if the json values are in the expected format
            JsonObject obj = jsonElement.getAsJsonObject();
            try {
                String amount = obj.get("amount").toString();
                String timestamp = obj.get("timestamp").toString();
                // the instantiation of the object will throw an exception if some of the values are not parsable
                transactionDto = new TransactionDto(amount, timestamp);

                // check if the time is in the future
                if (transactionDto.getTimestamp().getTime() - System.currentTimeMillis() > 0) {
                    return new ResponseEntity("Timestamp is in the future", HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } catch (Exception e) {
                return new ResponseEntity("Json not parsable", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (IllegalStateException ex) {
            return new ResponseEntity("Invalid json", HttpStatus.BAD_REQUEST);
        }

        // check if transaction is older than 60 seconds, if not, add it to the list
        try {
            long currentDate = System.currentTimeMillis();
            long transactionDate = transactionDto.getTimestamp().getTime();
            if ((currentDate - transactionDate) / 1000 > 60) {
                return new ResponseEntity("Older than 60s", HttpStatus.NO_CONTENT);
            }
            else {
                transactionService.addTransaction(transactionDto);
                return new ResponseEntity("Success", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity("Unexpected error", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteTransations() {
        transactionService.deleteTransations();
    }
}
