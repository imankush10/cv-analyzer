# 🧠 Dynamic CV Analyzer for HRs

An AI-powered CV parsing and analysis system for recruiters and HR professionals. It parses resumes, extracts key skills using NLP, and scores compatibility against job descriptions to streamline the candidate screening process.

---

## 🔧 Tech Stack

- **Frontend:** React  
- **Backend:** Spring Boot  
- **NLP:** Apache OpenNLP  
- **Database:** MySQL  

---

## 💼 Key Features

- **Resume Upload:** Upload resumes in PDF/DOC formats.
- **Skill Matrix:** Extracted skills mapped with candidates for structured viewing.
- **Job Match:** Calculates compatibility score between job descriptions and parsed resumes.
- **Admin Logs:** Tracks all parsing attempts and system-level actions.

---

## 📦 Modules

| Module         | Description                                            |
|----------------|--------------------------------------------------------|
| Resume Upload  | Handles resume file input and triggers backend parsing |
| Skill Matrix   | Visual representation of extracted candidate skills    |
| Job Match      | Calculates and displays job-resume match percentage    |

---

## 🧪 Evaluation

- **NLP-powered Parsing:** Uses Apache OpenNLP for entity and keyword extraction.
- **Match % Logic:** Scoring algorithm evaluates how well a resume fits a job description.
- **Logging System:** Admin logs track parsing attempts and errors for debugging and auditing.

---

## 🚀 Getting Started

> This assumes Java, Maven, Node.js, and MySQL are installed.

```bash
# Backend Setup
cd backend
mvn clean install
./mvnw spring-boot:run

# Frontend Setup
cd frontend
npm install
npm start
```

---

## 📁 Project Structure

```
├── frontend/         # React frontend
├── backend/          # Spring Boot backend
│   └── parser/       # Resume parsing logic with OpenNLP
├── database/         # SQL setup scripts
└── README.md         # Project documentation
```

---

## 📌 Future Improvements

- PDF/DOCX parsing accuracy enhancement  
- Skill synonym detection  
- REST API for third-party integration  
- Role-based access control for HR/admins

---

## 📝 License

MIT License – feel free to use, modify, and contribute!
