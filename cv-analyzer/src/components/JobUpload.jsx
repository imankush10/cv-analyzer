import React, { useState } from 'react';
import axios from 'axios';
import './JobUpload.css'; // We'll create this CSS file

const JobUpload = () => {
    const [jobData, setJobData] = useState({
        title: '',
        company: '',
        description: '',
        requirements: ''
    });
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});

    const validateForm = () => {
        const newErrors = {};
        
        if (!jobData.title.trim()) newErrors.title = 'Job title is required';
        if (!jobData.company.trim()) newErrors.company = 'Company name is required';
        if (!jobData.description.trim()) newErrors.description = 'Job description is required';
        if (!jobData.requirements.trim()) newErrors.requirements = 'Requirements are required';
        
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!validateForm()) {
            return;
        }

        setLoading(true);
        setErrors({});

        try {
            console.log('Submitting job data:', jobData); // Debug log
            const response = await axios.post('http://localhost:8080/api/jobs', jobData, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            
            console.log('Job upload response:', response.data); // Debug log
            alert('Job description uploaded successfully! 🎉');
            
            // Reset form
            setJobData({ 
                title: '', 
                company: '', 
                description: '', 
                requirements: '' 
            });
        } catch (error) {
            console.error('Error uploading job:', error.response?.data || error.message);
            alert(`Error uploading job description: ${error.response?.data?.message || error.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (field, value) => {
        setJobData(prev => ({
            ...prev,
            [field]: value
        }));
        
        // Clear error when user starts typing
        if (errors[field]) {
            setErrors(prev => ({
                ...prev,
                [field]: ''
            }));
        }
    };

    return (
        <div className="job-upload-container">
            <div className="job-upload-header">
                <h2>💼 Add New Job Description</h2>
                <p>Create a detailed job posting to match with candidate resumes</p>
            </div>
            
            <form onSubmit={handleSubmit} className="job-upload-form">
                <div className="form-group">
                    <label htmlFor="title">Job Title *</label>
                    <input
                        id="title"
                        type="text"
                        placeholder="e.g., Senior Full Stack Developer"
                        value={jobData.title}
                        onChange={(e) => handleInputChange('title', e.target.value)}
                        className={errors.title ? 'error' : ''}
                        required
                    />
                    {errors.title && <span className="error-message">{errors.title}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="company">Company Name *</label>
                    <input
                        id="company"
                        type="text"
                        placeholder="e.g., Tech Solutions Inc."
                        value={jobData.company}
                        onChange={(e) => handleInputChange('company', e.target.value)}
                        className={errors.company ? 'error' : ''}
                        required
                    />
                    {errors.company && <span className="error-message">{errors.company}</span>}
                </div>

                <div className="form-group">
                    <label htmlFor="description">Job Description *</label>
                    <textarea
                        id="description"
                        placeholder="Describe the role, responsibilities, and what the candidate will be doing..."
                        value={jobData.description}
                        onChange={(e) => handleInputChange('description', e.target.value)}
                        className={errors.description ? 'error' : ''}
                        rows="6"
                        required
                    />
                    {errors.description && <span className="error-message">{errors.description}</span>}
                    <small className="char-count">{jobData.description.length} characters</small>
                </div>

                <div className="form-group">
                    <label htmlFor="requirements">Requirements & Skills *</label>
                    <textarea
                        id="requirements"
                        placeholder="List required skills, experience, qualifications, etc..."
                        value={jobData.requirements}
                        onChange={(e) => handleInputChange('requirements', e.target.value)}
                        className={errors.requirements ? 'error' : ''}
                        rows="6"
                        required
                    />
                    {errors.requirements && <span className="error-message">{errors.requirements}</span>}
                    <small className="char-count">{jobData.requirements.length} characters</small>
                </div>

                <div className="form-actions">
                    <button 
                        type="button" 
                        onClick={() => setJobData({ title: '', company: '', description: '', requirements: '' })}
                        className="btn-secondary"
                        disabled={loading}
                    >
                        Clear Form
                    </button>
                    <button 
                        type="submit" 
                        className="btn-primary"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="spinner"></span>
                                Uploading...
                            </>
                        ) : (
                            <>
                                📤 Upload Job Description
                            </>
                        )}
                    </button>
                </div>
            </form>

            <div className="upload-tips">
                <h3>💡 Tips for Better Job Descriptions</h3>
                <ul>
                    <li>Be specific about required technical skills</li>
                    <li>Include years of experience needed</li>
                    <li>Mention specific technologies, frameworks, or tools</li>
                    <li>Describe the team structure and work environment</li>
                </ul>
            </div>
        </div>
    );
};

export default JobUpload;
