package com.cvanalyzer.service;

import com.cvanalyzer.model.Skill;
import com.cvanalyzer.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SkillDataInitializer {

    @Autowired
    private SkillRepository skillRepository;

    @PostConstruct
    public void initializeSkills() {
        if (skillRepository.count() == 0) {
            // Programming Languages
            skillRepository.save(createSkill("JavaScript", "Programming Language", 8, "js,javascript,ecmascript"));
            skillRepository.save(createSkill("TypeScript", "Programming Language", 7, "typescript,ts"));
            skillRepository.save(createSkill("Python", "Programming Language", 9, "python,py"));
            skillRepository.save(createSkill("Java", "Programming Language", 8, "java"));
            skillRepository.save(createSkill("C++", "Programming Language", 7, "cpp,c++,cplusplus"));
            skillRepository.save(createSkill("SQL", "Programming Language", 8, "sql,mysql,postgresql"));

            // Frameworks
            skillRepository.save(createSkill("React", "Framework", 8, "react,reactjs,react.js"));
            skillRepository.save(createSkill("Next.js", "Framework", 7, "nextjs,next.js,next"));
            skillRepository.save(createSkill("Spring Boot", "Framework", 8, "springboot,spring-boot,spring"));
            skillRepository.save(createSkill("Node.js", "Framework", 7, "nodejs,node.js,node"));
            skillRepository.save(createSkill("Express.js", "Framework", 6, "expressjs,express.js,express"));
            skillRepository.save(createSkill("React Native", "Framework", 6, "react-native,reactnative"));

            // Tools & Technologies
            skillRepository.save(createSkill("Git", "Tool", 6, "git,github,version control"));
            skillRepository.save(createSkill("Docker", "Tool", 7, "docker,containerization"));
            skillRepository.save(createSkill("Firebase", "Tool", 6, "firebase,google firebase"));
            skillRepository.save(createSkill("MySQL", "Database", 7, "mysql,my sql"));
            skillRepository.save(createSkill("Tailwind CSS", "Framework", 5, "tailwind,tailwindcss,tailwind css"));

            // Libraries
            skillRepository.save(createSkill("OpenNLP", "Library", 6, "opennlp,open nlp"));
            skillRepository.save(createSkill("TensorFlow", "Library", 8, "tensorflow,tensor flow"));

            System.out.println("Skills database initialized with sample data!");
        }
    }

    private Skill createSkill(String name, String category, Integer weight, String synonyms) {
        Skill skill = new Skill(name, category, weight);
        skill.setSynonyms(synonyms);
        return skill;
    }
}
