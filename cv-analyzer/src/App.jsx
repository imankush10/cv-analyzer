import React, { useState } from 'react';
import './App.css';
import ResumeUpload from './components/ResumeUpload';
import JobUpload from './components/JobUpload';
import JobMatcher from './components/JobMatcher';
import Dashboard from './components/Dashboard';
import AdminPanel from './components/AdminPanel';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderActiveComponent = () => {
    switch(activeTab) {
      case 'dashboard':
        return <Dashboard />;
      case 'resume-upload':
        return <ResumeUpload />;
      case 'job-upload':
        return <JobUpload />;
      case 'job-matcher':
        return <JobMatcher />;
      case 'admin':
        return <AdminPanel />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="App">
      <header className="app-header">
        <h1>🎯 Dynamic CV Analyzer</h1>
        <p>AI-Powered Resume Analysis & Job Matching Platform</p>
      </header>

      <nav className="app-navigation">
        <button 
          className={activeTab === 'dashboard' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('dashboard')}
        >
          📊 Dashboard
        </button>
        <button 
          className={activeTab === 'resume-upload' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('resume-upload')}
        >
          📄 Upload Resume
        </button>
        <button 
          className={activeTab === 'job-upload' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('job-upload')}
        >
          💼 Add Job
        </button>
        <button 
          className={activeTab === 'job-matcher' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('job-matcher')}
        >
          🔍 Job Matcher
        </button>
        <button 
          className={activeTab === 'admin' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('admin')}
        >
          ⚙️ Admin Panel
        </button>
      </nav>

      <main className="app-main">
        {renderActiveComponent()}
      </main>

      <footer className="app-footer">
        <p>Built with React + Spring Boot + OpenNLP + MySQL</p>
      </footer>
    </div>
  );
}

export default App;
