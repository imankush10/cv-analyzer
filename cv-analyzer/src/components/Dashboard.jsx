import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalResumes: 0,
    totalJobs: 0,
    successfulParses: 0,
    failedParses: 0,
    recentMatches: []
  });
  const [recentResumes, setRecentResumes] = useState([]);
  const [recentJobs, setRecentJobs] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [resumesRes, jobsRes] = await Promise.all([
        axios.get('http://localhost:8080/api/resumes'),
        axios.get('http://localhost:8080/api/jobs')
      ]);

      const resumes = resumesRes.data;
      const jobs = jobsRes.data;

      setStats({
        totalResumes: resumes.length,
        totalJobs: jobs.length,
        successfulParses: resumes.filter(r => r.parseStatus === 'SUCCESS').length,
        failedParses: resumes.filter(r => r.parseStatus === 'FAILED').length,
        recentMatches: []
      });

      setRecentResumes(resumes.slice(-5));
      setRecentJobs(jobs.slice(-5));
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  const getStatusColor = (status) => {
    switch(status) {
      case 'SUCCESS': return '#4CAF50';
      case 'FAILED': return '#f44336';
      case 'PROCESSING': return '#ff9800';
      default: return '#9e9e9e';
    }
  };

  return (
    <div className="dashboard">
      <h2>📊 System Overview</h2>
      
      <div className="stats-grid">
        <div className="stat-card">
          <h3>📄 Total Resumes</h3>
          <div className="stat-number">{stats.totalResumes}</div>
        </div>
        <div className="stat-card">
          <h3>💼 Total Jobs</h3>
          <div className="stat-number">{stats.totalJobs}</div>
        </div>
        <div className="stat-card success">
          <h3>✅ Successful Parses</h3>
          <div className="stat-number">{stats.successfulParses}</div>
        </div>
        <div className="stat-card error">
          <h3>❌ Failed Parses</h3>
          <div className="stat-number">{stats.failedParses}</div>
        </div>
      </div>

      <div className="dashboard-content">
        <div className="recent-section">
          <h3>📋 Recent Resumes</h3>
          <div className="recent-list">
            {recentResumes.map(resume => (
              <div key={resume.id} className="recent-item">
                <div className="item-info">
                  <strong>{resume.fileName}</strong>
                  <span className="item-date">
                    {new Date(resume.uploadDate).toLocaleDateString()}
                  </span>
                </div>
                <div 
                  className="status-badge"
                  style={{ backgroundColor: getStatusColor(resume.parseStatus) }}
                >
                  {resume.parseStatus}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="recent-section">
          <h3>💼 Recent Jobs</h3>
          <div className="recent-list">
            {recentJobs.map(job => (
              <div key={job.id} className="recent-item">
                <div className="item-info">
                  <strong>{job.title}</strong>
                  <span className="item-company">{job.company}</span>
                  <span className="item-date">
                    {new Date(job.createdAt).toLocaleDateString()}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
