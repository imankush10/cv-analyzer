package com.cvanalyzer.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NLPProcessingService {

    // Common programming skills and technologies
    private static final Set<String> TECH_SKILLS = Set.of(
            "java", "python", "javascript", "react", "angular", "vue", "node.js", "spring boot",
            "mysql", "postgresql", "mongodb", "redis", "docker", "kubernetes", "aws", "azure",
            "git", "jenkins", "maven", "gradle", "html", "css", "typescript", "php", "c++",
            "c#", ".net", "ruby", "go", "rust", "scala", "kotlin", "swift", "flutter", "django",
            "flask", "express", "fastapi", "hibernate", "jpa", "rest api", "graphql", "microservices",
            "devops", "ci/cd", "terraform", "ansible", "linux", "unix", "bash", "powershell"
    );

    private static final Set<String> SOFT_SKILLS = Set.of(
            "leadership", "communication", "teamwork", "problem solving", "analytical thinking",
            "project management", "time management", "adaptability", "creativity", "critical thinking",
            "collaboration", "mentoring", "presentation", "negotiation", "customer service"
    );

    public Map<String, Object> processResumeText(String text) {
        Map<String, Object> result = new HashMap<>();

        // Extract basic information
        result.put("candidateName", extractCandidateName(text));
        result.put("email", extractEmail(text));
        result.put("phoneNumber", extractPhoneNumber(text));

        // Extract skills
        result.put("technicalSkills", extractTechnicalSkills(text));
        result.put("softSkills", extractSoftSkills(text));

        // Extract experience
        result.put("workExperience", extractWorkExperience(text));
        result.put("education", extractEducation(text));

        // Calculate years of experience
        result.put("totalExperience", calculateTotalExperience(text));

        return result;
    }

    private String extractCandidateName(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.length() > 0 && !line.contains("@") && !line.contains("+91")) {
                // Check if it looks like a name (2-4 words)
                String[] words = line.split("\\s+");
                if (words.length >= 2 && words.length <= 4) {
                    boolean isName = true;
                    for (String word : words) {
                        if (!word.matches("[A-Za-z]+")) {
                            isName = false;
                            break;
                        }
                    }
                    if (isName) {
                        return line;
                    }
                }
            }
        }
        return null;
    }


    private String extractEmail(String text) {
        Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        Matcher matcher = emailPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String extractPhoneNumber(String text) {
        // Pattern for various phone number formats
        Pattern phonePattern = Pattern.compile("(\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}");
        Matcher matcher = phonePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private List<String> extractTechnicalSkills(String text) {
        List<String> foundSkills = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (String skill : TECH_SKILLS) {
            if (lowerText.contains(skill.toLowerCase())) {
                foundSkills.add(skill);
            }
        }

        return foundSkills;
    }

    private List<String> extractSoftSkills(String text) {
        List<String> foundSkills = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (String skill : SOFT_SKILLS) {
            if (lowerText.contains(skill.toLowerCase())) {
                foundSkills.add(skill);
            }
        }

        return foundSkills;
    }

    private List<String> extractWorkExperience(String text) {
        List<String> experiences = new ArrayList<>();

        // Look for company names and job titles
        Pattern companyPattern = Pattern.compile("(?i)(worked at|employed by|company:|organization:)\\s*([A-Za-z\\s&.,]+)");
        Matcher matcher = companyPattern.matcher(text);

        while (matcher.find()) {
            experiences.add(matcher.group(2).trim());
        }

        return experiences;
    }

    private List<String> extractEducation(String text) {
        List<String> education = new ArrayList<>();

        // Look for degrees and institutions
        Pattern degreePattern = Pattern.compile("(?i)(bachelor|master|phd|b\\.?tech|m\\.?tech|mba|b\\.?sc|m\\.?sc|b\\.?com|m\\.?com)");
        Matcher matcher = degreePattern.matcher(text);

        while (matcher.find()) {
            education.add(matcher.group().trim());
        }

        return education;
    }

    private Integer calculateTotalExperience(String text) {
        // Look for experience mentions like "5 years", "3+ years", etc.
        Pattern expPattern = Pattern.compile("(?i)(\\d+)\\+?\\s*(years?|yrs?)\\s*(of\\s*)?(experience|exp)");
        Matcher matcher = expPattern.matcher(text);

        int maxExperience = 0;
        while (matcher.find()) {
            try {
                int years = Integer.parseInt(matcher.group(1));
                maxExperience = Math.max(maxExperience, years);
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }

        return maxExperience > 0 ? maxExperience : null;
    }
}
