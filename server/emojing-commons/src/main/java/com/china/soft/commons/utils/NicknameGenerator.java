package com.china.soft.commons.utils;

import java.util.Random;

/**
 * @author admin
 */
public class NicknameGenerator {
    private static final String[] adjectives = {
            "Happy", "Smiling", "Sunny", "Clever", "Cool", "Lucky", "Jolly", "Creative", "Friendly", "Adventurous"
    };

    private static final String[] nouns = {
            "Cat", "Dog", "Explorer", "Star", "Hiker", "Ninja", "Sailor", "Artist", "Engineer", "Captain"
    };

    public static String generateRandomNickname() {
        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];

        return adjective + noun;
    }
}
