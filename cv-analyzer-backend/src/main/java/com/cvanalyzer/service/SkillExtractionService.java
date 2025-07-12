package com.cvanalyzer.service;

import com.cvanalyzer.model.Resume;
import com.cvanalyzer.model.ResumeSkill;
import com.cvanalyzer.model.Skill;
import com.cvanalyzer.model.JobDescription;
import com.cvanalyzer.model.JobSkill;
import com.cvanalyzer.repository.ResumeSkillRepository;
import com.cvanalyzer.repository.SkillRepository;
import com.cvanalyzer.repository.JobSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillExtractionService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ResumeSkillRepository resumeSkillRepository;

    @Autowired
    private JobSkillRepository jobSkillRepository;

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;


    public List<ResumeSkill> extractSkillsFromResume(Resume resume) {
        List<Skill> allSkills = skillRepository.findByIsActiveTrue();
        List<ResumeSkill> extractedSkills = new ArrayList<>();

        String text = resume.getExtractedText().toLowerCase();

        for (Skill skill : allSkills) {
            Double confidence = calculateSkillMatch(text, skill);

            if (confidence > 0.5) { // Threshold for skill detection
                ResumeSkill resumeSkill = new ResumeSkill();
                resumeSkill.setResume(resume);
                resumeSkill.setSkill(skill);
                resumeSkill.setMatchConfidence(confidence);
                resumeSkill.setProficiencyLevel(estimateProficiency(text, skill));
                resumeSkill.setExtractedContext(extractSkillContext(text, skill.getName()));

                extractedSkills.add(resumeSkill);
            }
        }

        return resumeSkillRepository.saveAll(extractedSkills);
    }

    public List<String> extractSkillsFromText(String text) {
        // Use OpenNLP to extract skills from any text
        // This is a simplified version - you'll need to implement proper NLP logic
        String[] sentences = sentenceDetector.sentDetect(text);
        List<String> skills = new ArrayList<>();

        for (String sentence : sentences) {
            String[] tokens = tokenizer.tokenize(sentence);
            // Add your skill extraction logic here using OpenNLP
            // You can use named entity recognition or pattern matching
            skills.addAll(extractSkillsFromTokens(tokens));
        }

        return skills.stream().distinct().collect(Collectors.toList());
    }

    public void extractSkillsFromJobDescription(JobDescription job) {
        String fullText = job.getDescription() + " " + job.getRequirements();
        List<String> extractedSkills = extractSkillsFromText(fullText);

        // Save job skills to database
        for (String skillName : extractedSkills) {
            Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                    .orElseGet(() -> {
                        Skill newSkill = new Skill();
                        newSkill.setName(skillName);
                        newSkill.setCategory("Technical"); // Default category
                        return skillRepository.save(newSkill);
                    });

            JobSkill jobSkill = new JobSkill();
            jobSkill.setJobDescription(job);
            jobSkill.setSkill(skill);
            jobSkill.setImportanceLevel(3); // Default importance
            jobSkill.setIsRequired(true);

            jobSkillRepository.save(jobSkill);
        }
    }

    private List<String> extractSkillsFromTokens(String[] tokens) {
        List<String> extractedSkills = new ArrayList<>();

        // Simple keyword matching - you can enhance this with more sophisticated NLP
        List<Skill> allSkills = skillRepository.findByIsActiveTrue();

        for (String token : tokens) {
            for (Skill skill : allSkills) {
                if (skill.getName().toLowerCase().contains(token.toLowerCase()) ||
                        token.toLowerCase().contains(skill.getName().toLowerCase())) {
                    extractedSkills.add(skill.getName());
                }
            }
        }

        return extractedSkills;
    }

    private Double calculateSkillMatch(String text, Skill skill) {
        // Check main skill name
        if (text.contains(skill.getName().toLowerCase())) {
            return 1.0;
        }

        // Check synonyms
        if (skill.getSynonyms() != null) {
            String[] synonyms = skill.getSynonyms().split(",");
            for (String synonym : synonyms) {
                if (text.contains(synonym.trim().toLowerCase())) {
                    return 0.8; // Slightly lower confidence for synonyms
                }
            }
        }

        return 0.0;
    }

    private Integer estimateProficiency(String text, Skill skill) {
        // Simple heuristic - you can enhance this with NLP
        String skillContext = extractSkillContext(text, skill.getName());

        if (skillContext.contains("expert") || skillContext.contains("advanced")) {
            return 5;
        } else if (skillContext.contains("proficient") || skillContext.contains("experienced")) {
            return 4;
        } else if (skillContext.contains("intermediate")) {
            return 3;
        } else if (skillContext.contains("basic") || skillContext.contains("beginner")) {
            return 2;
        }

        return 3; // Default intermediate level
    }

    private String extractSkillContext(String text, String skillName) {
        // Find the skill in text and extract surrounding context
        int index = text.toLowerCase().indexOf(skillName.toLowerCase());
        if (index == -1) return "";

        int start = Math.max(0, index - 50);
        int end = Math.min(text.length(), index + skillName.length() + 50);

        return text.substring(start, end);
    }
}
