import React, { useState, useEffect } from "react";
import axios from "axios";

const ResumeUpload = () => {
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [resumes, setResumes] = useState([]);
  const [selectedResume, setSelectedResume] = useState(null);
  const [skills, setSkills] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchResumes();
  }, []);

  const fetchResumes = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/resumes");
      setResumes(response.data);
    } catch (error) {
      console.error("Error fetching resumes:", error);
      alert("Error fetching resumes. Please check if the server is running.");
    }
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!file) {
      alert("Please select a file");
      return;
    }

    setUploading(true);
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/resumes/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      alert("Resume uploaded successfully!");
      setFile(null);
      fetchResumes();

      if (response.data.id) {
        await extractSkills(response.data.id);
      }
    } catch (error) {
      console.error("Upload error:", error);
      alert(
        "Error uploading resume: " +
          (error.response?.data?.message || error.message)
      );
    } finally {
      setUploading(false);
    }
  };

  const extractSkills = async (resumeId) => {
    try {
      setLoading(true);
      await axios.post(
        `http://localhost:8080/api/resumes/${resumeId}/extract-skills`
      );
      alert("Skills extracted successfully!");
      fetchResumes();
    } catch (error) {
      console.error("Error extracting skills:", error);
      alert(
        "Error extracting skills: " +
          (error.response?.data?.message || error.message)
      );
    } finally {
      setLoading(false);
    }
  };

  const viewSkills = async (resumeId) => {
    try {
      setLoading(true);
      const response = await axios.get(
        `http://localhost:8080/api/resumes/${resumeId}/skills`
      );
      setSkills(response.data);
      setSelectedResume(resumeId);
    } catch (error) {
      console.error("Error fetching skills:", error);
      alert(
        "Error fetching skills: " +
          (error.response?.data?.message || error.message)
      );
    } finally {
      setLoading(false);
    }
  };

  const deleteResume = async (resumeId) => {
    if (window.confirm("Are you sure you want to delete this resume?")) {
      try {
        await axios.delete(`http://localhost:8080/api/resumes/${resumeId}`);
        alert("Resume deleted successfully!");
        fetchResumes();
        if (selectedResume === resumeId) {
          setSelectedResume(null);
          setSkills([]);
        }
      } catch (error) {
        console.error("Error deleting resume:", error);
        alert(
          "Error deleting resume: " +
            (error.response?.data?.message || error.message)
        );
      }
    }
  };

  const closeModal = () => {
    setSelectedResume(null);
    setSkills([]);
  };

  return (
    <div className="resume-upload">
      <h2 className="text-2xl font-bold mb-6 flex items-center" style={{color: "#2c3e50"}}>
        📄 Resume Management
      </h2>

      {/* Upload Section */}
      <div className="upload-section">
        <h3 className="text-lg font-semibold mb-4" style={{color: "#2980b9"}}>Upload New Resume</h3>
        <form onSubmit={handleUpload} className="upload-form">
          <div className="file-input-wrapper">
            <input
              type="file"
              accept=".pdf,.doc,.docx"
              onChange={handleFileChange}
              className="file-input"
              id="file-input"
            />
            <label htmlFor="file-input" className="file-label">
              {file ? (
                <span>
                  <strong>Selected:</strong> {file.name}
                  <br />
                  <span style={{color: "#2980b9", fontSize: "0.9em"}}>Click to change file</span>
                </span>
              ) : (
                <span>
                  <strong>Choose PDF or Word file...</strong>
                  <br />
                  <span style={{fontSize: "0.9em"}}>Click to browse files</span>
                </span>
              )}
            </label>
          </div>
          <button
            type="submit"
            disabled={uploading || !file}
            className="upload-btn"
          >
            {uploading ? "⏳ Uploading..." : "📤 Upload Resume"}
          </button>
        </form>
      </div>

      {/* Uploaded Resumes */}
      <div>
        <h3 className="text-lg font-semibold mb-4" style={{color: "#2980b9"}}>
          📋 Uploaded Resumes ({resumes.length})
        </h3>

        {loading && (
          <div className="text-center py-4">
            <span style={{color: "#2980b9"}}>⏳ Loading...</span>
          </div>
        )}

        <div className="resumes-grid">
          {resumes.map((resume) => (
            <div key={resume.id} className="resume-card">
              <div className="resume-header">
                <h4>{resume.fileName}</h4>
                <span
                  className="status-badge"
                  style={{
                    background:
                      resume.parseStatus === "SUCCESS"
                        ? "#00b894"
                        : resume.parseStatus === "FAILED"
                        ? "#e74c3c"
                        : "#fdcb6e",
                  }}
                >
                  {resume.parseStatus}
                </span>
              </div>
              <div className="resume-info">
                <p>
                  <strong>Size:</strong> {(resume.fileSize / 1024).toFixed(2)} KB
                </p>
                <p>
                  <strong>Uploaded:</strong>{" "}
                  {new Date(resume.uploadDate).toLocaleDateString()}
                </p>
                {resume.extractedText && (
                  <p>
                    <strong>Text Length:</strong> {resume.extractedText.length} chars
                  </p>
                )}
              </div>
              <div className="resume-actions">
                <button
                  onClick={() => viewSkills(resume.id)}
                  disabled={loading}
                  className="action-btn primary"
                >
                  👁️ View Skills
                </button>
                <button
                  onClick={() => extractSkills(resume.id)}
                  disabled={loading}
                  className="action-btn secondary"
                >
                  🔍 Extract Skills
                </button>
                <button
                  onClick={() => deleteResume(resume.id)}
                  disabled={loading}
                  className="action-btn danger"
                >
                  🗑️ Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Skills Modal */}
      {selectedResume && (
        <div className="skills-modal">
          <div className="modal-content">
            <div className="modal-header">
              <h3>🎯 Extracted Skills</h3>
              <button onClick={closeModal} className="close-btn">
                ✕
              </button>
            </div>

            {skills.length > 0 ? (
              <>
                <div className="skills-stats">
                  <div className="skill-stat">
                    <span className="skill-stat-number">{skills.length}</span>
                    <span className="skill-stat-label">Total Skills</span>
                  </div>
                  <div className="skill-stat">
                    <span className="skill-stat-number">
                      {skills.filter((s) => s.matchConfidence > 0.8).length}
                    </span>
                    <span className="skill-stat-label">High Confidence</span>
                  </div>
                  <div className="skill-stat">
                    <span className="skill-stat-number">
                      {Math.round(
                        (skills.reduce((acc, s) => acc + s.matchConfidence, 0) /
                          skills.length) *
                          100
                      )}
                      %
                    </span>
                    <span className="skill-stat-label">Avg Confidence</span>
                  </div>
                </div>
                <div className="skills-grid">
                  {skills.map((skill) => (
                    <div key={skill.id} className="skill-tag">
                      <span className="skill-name">{skill.skill.name}</span>
                      <span
                        className={`skill-confidence ${
                          skill.matchConfidence > 0.8
                            ? "high"
                            : skill.matchConfidence > 0.6
                            ? "medium"
                            : "low"
                        }`}
                      >
                        {(skill.matchConfidence * 100).toFixed(1)}%
                      </span>
                    </div>
                  ))}
                </div>
              </>
            ) : (
              <div className="empty-skills">
                <div className="empty-skills-icon">🤔</div>
                <h4>No skills found</h4>
                <p>
                  Try extracting skills first, or check if the resume contains
                  recognizable skills.
                </p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default ResumeUpload;
