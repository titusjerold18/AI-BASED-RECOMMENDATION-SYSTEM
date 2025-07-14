//package com.asvikaa.recommender.recipeRecommender;
//// SmartRecipeRecommenderBackend.java
//// Combined Filter + Mahout Recommendation Backend using Spring Boot
//
//import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
//import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
//import org.springframework.web.bind.annotation.*;
//import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
//import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
//import org.apache.mahout.cf.taste.similarity.UserSimilarity;
//import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
//import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
//import org.apache.mahout.cf.taste.recommender.Recommender;
//import org.apache.mahout.cf.taste.recommender.RecommendedItem;
//import org.apache.mahout.cf.taste.model.DataModel;
//import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
//import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
//
//
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.stream.Collectors;

/*@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SmartRecipeRecommenderBackend {

    private final File ratingFile = new File(
            getClass().getClassLoader().getResource("data/ratings.csv").getFile()
    );
    private final File recipeFile = new File(
            getClass().getClassLoader().getResource("data/recipes.csv").getFile()
    );

    /*@GetMapping("/recommend")
    public List<String> recommendRecipes(@RequestParam long userId) throws Exception {
        DataModel model = new FileDataModel(
                new File(getClass().getClassLoader().getResource("data/ratings.csv").getFile())
        );

        UserSimilarity userSimilarity = new LogLikelihoodSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, userSimilarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);

        List<RecommendedItem> recommendations = recommender.recommend(userId, 5);

        System.out.println("Raw Mahout recommendations:");
        for (RecommendedItem item : recommendations) {
            System.out.println("→ itemID=" + item.getItemID() + ", score=" + item.getValue());
        }

        Map<Long, String> recipeNames = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(
                new File(getClass().getClassLoader().getResource("data/recipes.csv").getFile())));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", 3);
            if (parts.length >= 2) {
                long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                recipeNames.put(id, name);
            }
        }
        br.close();

        List<String> result = new ArrayList<>();
        for (RecommendedItem item : recommendations) {
            String name = recipeNames.get(item.getItemID());
            if (name != null) result.add(name);
        }

        System.out.println("Final result: " + result);
        return result;
    }


    @GetMapping("/filter")
    public List<String> filterByIngredients(@RequestParam String ingredients) throws IOException {
        Set<String> inputSet = new HashSet<>(Arrays.asList(ingredients.toLowerCase().split(",")));
        List<String> matched = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(recipeFile));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", 3);
            if (parts.length < 3) continue;
            String recipeName = parts[1];
            String[] recipeIngredients = parts[2].replace("\"", "").toLowerCase().split(",");

            int matchedCount = 0;
            for (String ingredient : recipeIngredients) {
                if (inputSet.contains(ingredient.trim())) matchedCount++;
            }

            if ((double) matchedCount / recipeIngredients.length >= 0.7) {
                matched.add(recipeName);
            }
        }
        br.close();
        return matched;
    }


    @GetMapping("/recommend")
    public List<String> recommendRecipes(@RequestParam(defaultValue = "1") long userId) throws Exception {
        // 1. Load data
        File ratingsFile = new File(getClass().getClassLoader().getResource("data/ratings.csv").getFile());
        DataModel model = new FileDataModel(ratingsFile);

        // 2. Use item-based recommender (better for sparse data)
        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        Recommender recommender = new GenericItemBasedRecommender(model, similarity);

        // 3. Get recommendations (try more items)
        List<RecommendedItem> recommendations = recommender.recommend(userId, 5);

        // 4. Load recipe names
        Map<Long, String> recipeNames = Files.lines(Paths.get(
                        getClass().getClassLoader().getResource("data/recipes.csv").toURI()))
                .map(line -> line.split(",", 3))
                .filter(parts -> parts.length >= 2)
                .collect(Collectors.toMap(
                        parts -> Long.parseLong(parts[0].trim()),
                        parts -> parts[1].trim()
                ));

        // 5. Return results
        return recommendations.stream()
                .map(item -> recipeNames.get(item.getItemID()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @GetMapping("/recommend/filter")
    public List<String> combinedRecommendation(@RequestParam long userId, @RequestParam String ingredients) throws Exception {
        List<String> recommended = recommendRecipes(userId);
        List<String> filtered = filterByIngredients(ingredients);

        List<String> finalList = new ArrayList<>();
        for (String recipe : recommended) {
            if (filtered.contains(recipe)) {
                finalList.add(recipe);
            }
        }
        return finalList;
    }

    private Map<Long, String> loadRecipeNames() throws IOException {
        Map<Long, String> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(recipeFile));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", 3);
            if (parts.length < 2) continue;
            if (parts[0].startsWith("#") || parts[0].equalsIgnoreCase("recipeId")) continue;
            try {
                long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                map.put(id, name);
            } catch (NumberFormatException e) {
                System.err.println("Skipping invalid row: " + line);
            }
        }
        br.close();
        return map;
    }
}*/

//
//package com.asvikaa.recommender.recipeRecommender;
//
//import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
//import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
//import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
//import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
//import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
//import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
//import org.apache.mahout.cf.taste.model.DataModel;
//import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
//import org.apache.mahout.cf.taste.recommender.RecommendedItem;
//import org.apache.mahout.cf.taste.recommender.Recommender;
//import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
//import org.apache.mahout.cf.taste.similarity.UserSimilarity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:3000",
//        allowedHeaders = "*",
//        allowCredentials = "true")
//public class SmartRecipeRecommenderBackend {
//
//    private final File ratingFile;
//    private final File recipeFile;
//
//    public SmartRecipeRecommenderBackend() throws IOException {
//        this.ratingFile = new File(getClass().getClassLoader().getResource("data/ratings.csv").getFile());
//        this.recipeFile = new File(getClass().getClassLoader().getResource("data/recipes.csv").getFile());
//    }
//
//    @GetMapping("/recommend")
//    public List<String> recommendRecipes(@RequestParam(defaultValue = "1") long userId) throws Exception {
//        return getRecommendations(userId, null, 0);
//    }
//
//    @GetMapping("/filter")
//    public List<String> filterRecipesByIngredients(
//            @RequestParam String ingredients,
//            @RequestParam(defaultValue = "1.0") double matchThreshold) throws IOException {
//
//        Set<String> desiredIngredients = parseIngredients(ingredients);
//        Map<Long, RecipeData> allRecipes = loadRecipeData();
//
//        return allRecipes.values().stream()
//                .filter(recipe -> recipe.matchesIngredients(desiredIngredients, matchThreshold))
//                .map(RecipeData::getName)
//                .collect(Collectors.toList());
//    }
//
//    @GetMapping("/recommend/filter")
//    public List<String> combinedRecommendation(
//            @RequestParam long userId,
//            @RequestParam String ingredients,
//            @RequestParam(defaultValue = "0.7cd ") double matchThreshold) throws Exception {
//
//        Set<String> desiredIngredients = parseIngredients(ingredients);
//        return getRecommendations(userId, desiredIngredients, matchThreshold);
//    }
//
//    @GetMapping("/smart-recommend")
//    public List<String> smartRecommend(
//            @RequestParam Set<String> userIngredients,
//            @RequestParam(defaultValue = "1") long userId) throws Exception {
//
//        // 1. Get standard recommendations
//        List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
//
//        // 2. Load recipe data with required ingredients
//        Map<Long, RecipeData> recipes = loadRecipeData();
//
//        // 3. Filter recipes user can actually make
//        return recommendations.stream()
//                .map(item -> recipes.get(item.getItemID()))
//                .filter(Objects::nonNull)
//                .filter(recipe -> recipe.canMakeWith(userIngredients))
//                .map(RecipeData::getName)
//                .collect(Collectors.toList());
//    }
//
//    private List<String> getRecommendations(long userId, Set<String> desiredIngredients, double matchThreshold) throws Exception {
//        // 1. Load data model
//        DataModel model = new FileDataModel(ratingFile);
//
//        // 2. Create recommender (item-based works better for sparse data)
//        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
//        Recommender recommender = new GenericItemBasedRecommender(model, similarity);
//
//        // 3. Get recommendations
//        List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
//
//        // 4. Load recipe data
//        Map<Long, RecipeData> recipes = loadRecipeData();
//
//        // 5. Filter and return results
//        return recommendations.stream()
//                .map(item -> recipes.get(item.getItemID()))
//                .filter(Objects::nonNull)
//                .filter(recipe -> desiredIngredients == null ||
//                        recipe.matchesIngredients(desiredIngredients, matchThreshold))
//                .map(RecipeData::getName)
//                .limit(5)
//                .collect(Collectors.toList());
//    }
//
//    private Set<String> parseIngredients(String ingredients) {
//        return Arrays.stream(ingredients.split(","))
//                .map(String::trim)
//                .map(String::toLowerCase)
//                .collect(Collectors.toSet());
//    }
//
//    private Map<Long, RecipeData> loadRecipeData() throws IOException {
//        Map<Long, RecipeData> recipeMap = new HashMap<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(recipeFile))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (line.startsWith("#") || line.startsWith("recipeId")) continue;
//
//                String[] parts = line.split(",", 3);
//                if (parts.length < 3) continue;
//
//                try {
//                    long id = Long.parseLong(parts[0].trim());
//                    String name = parts[1].trim();
//                    Set<String> ingredients = Arrays.stream(parts[2].replace("\"", "").split(","))
//                            .map(String::trim)
//                            .map(String::toLowerCase)
//                            .collect(Collectors.toSet());
//
//                    recipeMap.put(id, new RecipeData(id, name, ingredients));
//                } catch (NumberFormatException e) {
//                    System.err.println("Skipping invalid row: " + line);
//                }
//            }
//        }
//        return recipeMap;
//    }
//
//    private static class RecipeData {
//        private final long id;
//        private final String name;
//        private final Set<String> mandatoryIngredients;  // Changed from generic 'ingredients'
//        private final Set<String> optionalIngredients;    // New field
//
//        public RecipeData(long id, String name, Set<String> mandatoryIngredients, Set<String> optionalIngredients) {
//            this.id = id;
//            this.name = name;
//            this.mandatoryIngredients = mandatoryIngredients;
//            this.optionalIngredients = optionalIngredients;
//        }
//
//        // New method for strict checking
//        public boolean canMakeWith(Set<String> userIngredients) {
//            return userIngredients.containsAll(this.mandatoryIngredients);
//        }
//
//        // Updated matching method
//        public boolean matchesIngredients(Set<String> desiredIngredients, double threshold) {
//            if (desiredIngredients == null || desiredIngredients.isEmpty()) {
//                return true;
//            }
//
//            // First check mandatory ingredients
//            if (!userIngredients.containsAll(this.mandatoryIngredients)) {
//                return false;
//            }
//
//            // Then apply threshold to desired ingredients
//            long matchCount = desiredIngredients.stream()
//                    .filter(desired ->
//                            this.mandatoryIngredients.contains(desired) ||
//                                    this.optionalIngredients.contains(desired))
//                    .count();
//
//            if (threshold == 1.0) {
//                return matchCount == desiredIngredients.size();
//            }
//            return (double) matchCount / desiredIngredients.size() >= threshold;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }
//}




































package com.asvikaa.recommender.recipeRecommender;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SmartRecipeRecommenderBackend {
    private final File ratingFile;
    private final File recipeFile;
    private final Recommender recommender;

    public SmartRecipeRecommenderBackend() throws IOException {
        this.ratingFile = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("data/ratings.csv")).getFile());
        this.recipeFile = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("data/recipes.csv")).getFile());

        DataModel model = new FileDataModel(ratingFile);
        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        this.recommender = new GenericItemBasedRecommender(model, similarity);
    }

    @GetMapping("/recommend")
    public List<String> recommendRecipes(@RequestParam(defaultValue = "1") long userId) {
        try {
            return getRecommendations(userId, null, 0);
        } catch (Exception e) {
            return Collections.singletonList("Error occurred while processing request");
        }
    }

    @GetMapping("/filter")
    public List<String> filterRecipesByIngredients(
            @RequestParam String ingredients,
            @RequestParam(defaultValue = "1.0") double matchThreshold) {
        try {
            Set<String> desiredIngredients = parseIngredients(ingredients);
            Map<Long, RecipeData> allRecipes = loadRecipeData();

            return allRecipes.values().stream()
                    .filter(recipe -> recipe.matchesIngredients(desiredIngredients, matchThreshold))
                    .map(RecipeData::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.singletonList("Error filtering recipes");
        }
    }

    @GetMapping("/recommend/filter")
    public List<String> combinedRecommendation(
            @RequestParam long userId,
            @RequestParam String ingredients,
            @RequestParam(defaultValue = "0.7") double matchThreshold) {
        try {
            Set<String> desiredIngredients = parseIngredients(ingredients);
            return getRecommendations(userId, desiredIngredients, matchThreshold);
        } catch (Exception e) {
            return Collections.singletonList("Error processing recommendation");
        }
    }
    @GetMapping("/strict-recommend")
    public List<String> strictRecommend(
            @RequestParam Set<String> userIngredients) {  // Removed userId parameter
        try {
            Map<Long, RecipeData> allRecipes = loadRecipeData();

            return allRecipes.values().stream()
                    .filter(recipe -> userHasAllIngredients(recipe, userIngredients))
                    .map(RecipeData::getName)
                    .limit(5)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.singletonList("Error processing request");
        }
    }

    private boolean userHasAllIngredients(RecipeData recipe, Set<String> userIngredients) {
        Set<String> normalizedRecipeIngredients = recipe.getIngredients().stream()
                .map(String::toLowerCase)
                .map(s -> s.replaceAll("(s|es)$", "")) // Remove plurals
                .collect(Collectors.toSet());

        Set<String> normalizedUserIngredients = userIngredients.stream()
                .map(String::toLowerCase)
                .map(s -> s.replaceAll("(s|es)$", ""))
                .collect(Collectors.toSet());

        return normalizedUserIngredients.containsAll(normalizedRecipeIngredients);
    }

    private List<String> getRecommendations(long userId, Set<String> desiredIngredients, double threshold) throws Exception {
        List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
        Map<Long, RecipeData> recipes = loadRecipeData();

        return recommendations.stream()
                .map(item -> recipes.get(item.getItemID()))
                .filter(Objects::nonNull)
                .filter(recipe -> desiredIngredients == null ||
                        recipe.matchesIngredients(desiredIngredients, threshold))
                .map(RecipeData::getName)
                .limit(5)
                .collect(Collectors.toList());
    }

    private Map<Long, RecipeData> loadRecipeData() throws IOException {
        Map<Long, RecipeData> recipeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(recipeFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.startsWith("recipeId")) continue;

                String[] parts = line.split(",", 3);
                if (parts.length < 3) continue;

                try {
                    long id = Long.parseLong(parts[0].trim());
                    String name = parts[1].trim();
                    Set<String> ingredients = parseIngredients(parts[2]);
                    recipeMap.put(id, new RecipeData(id, name, ingredients));
                } catch (NumberFormatException e) {
                    // Skip invalid rows
                }
            }
        }
        return recipeMap;
    }

    private Set<String> parseIngredients(String ingredientString) {
        return Arrays.stream(ingredientString.replace("\"", "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private static class RecipeData {
        private final long id;
        private final String name;
        private final Set<String> ingredients;

        public RecipeData(long id, String name, Set<String> ingredients) {
            this.id = id;
            this.name = name;
            this.ingredients = ingredients;
        }
        public Set<String> getIngredients() {
            return ingredients;
        }
        public boolean matchesIngredients(Set<String> desiredIngredients, double threshold) {
            if (desiredIngredients == null || desiredIngredients.isEmpty()) {
                return true;
            }

            long matchCount = desiredIngredients.stream()
                    .filter(desired -> ingredients.stream()
                            .anyMatch(recipeIng -> recipeIng.contains(desired)))
                    .count();

            if (threshold == 1.0) {
                return matchCount == desiredIngredients.size();
            }
            return (double) matchCount / desiredIngredients.size() >= threshold;
        }

        public String getName() {
            return name;
        }
    }
}
