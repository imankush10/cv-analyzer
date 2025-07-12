    import React, { useState, useEffect } from 'react';
    import axios from 'axios';

    const JobMatcher = () => {
        const [resumes, setResumes] = useState([]);
        const [jobs, setJobs] = useState([]);
        const [selectedResume, setSelectedResume] = useState('');
        const [selectedJob, setSelectedJob] = useState('');
        const [matchResult, setMatchResult] = useState(null);

        useEffect(() => {
            fetchResumes();
            fetchJobs();
        }, []);

        const fetchResumes = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/resumes');
                setResumes(response.data);
            } catch (error) {
                console.error('Error fetching resumes:', error);
            }
        };

        const fetchJobs = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/jobs');
                setJobs(response.data);
            } catch (error) {
                console.error('Error fetching jobs:', error);
            }
        };

        const handleMatch = async () => {
            if (!selectedResume || !selectedJob) {
                alert('Please select both a resume and job');
                return;
            }

            try {
                const response = await axios.post(
                    `http://localhost:8080/api/jobs/${selectedJob}/match/${selectedResume}`
                );
                setMatchResult(response.data);
            } catch (error) {
                alert('Error calculating match');
            }
        };

        return (
            <div className="job-matcher">
                <h2>Job-Resume Matcher</h2>
                
                <div className="selection-area">
                    <select 
                        value={selectedResume} 
                        onChange={(e) => setSelectedResume(e.target.value)}
                    >
                        <option value="">Select Resume</option>
                        {resumes.map(resume => (
                            <option key={resume.id} value={resume.id}>
                                {resume.fileName}
                            </option>
                        ))}
                    </select>

                    <select 
                        value={selectedJob} 
                        onChange={(e) => setSelectedJob(e.target.value)}
                    >
                        <option value="">Select Job</option>
                        {jobs.map(job => (
                            <option key={job.id} value={job.id}>
                                {job.title} - {job.company}
                            </option>
                        ))}
                    </select>

                    <button onClick={handleMatch}>Calculate Match</button>
                </div>

                {matchResult && (
                    <div className="match-result">
                        <h3>Match Result</h3>
                        <p><strong>Match Percentage:</strong> {matchResult.matchPercentage.toFixed(2)}%</p>
                        <p><strong>Match Level:</strong> {matchResult.matchLevel}</p>
                        
                        <div className="skills-section">
                            <h4>Matched Skills:</h4>
                            <ul>
                                {matchResult.matchedSkills.map((skill, index) => (
                                    <li key={index} className="matched-skill">{skill}</li>
                                ))}
                            </ul>
                            
                            <h4>Missing Skills:</h4>
                            <ul>
                                {matchResult.missingSkills.map((skill, index) => (
                                    <li key={index} className="missing-skill">{skill}</li>
                                ))}
                            </ul>
                        </div>
                    </div>
                )}
            </div>
        );
    };

    export default JobMatcher;
