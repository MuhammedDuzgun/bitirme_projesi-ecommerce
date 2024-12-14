package com.bitirmeprojesi.ecommerce.ai.services;

import com.bitirmeprojesi.ecommerce.exception.ProductException;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import org.json.JSONException;

public interface AiChatBotService {

    ApiResponse aiChatBot(String prompt,Long productId,Long userId) throws ProductException, JSONException;
}
