import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AdminPanel = () => {
  const [logs, setLogs] = useState([]);
  const [stats, setStats] = useState({});
  const [failedParses, setFailedParses] = useState([]);

  useEffect(() => {
    fetchAdminData();
  }, []);

  const fetchAdminData = async () => {
    try {
      const [logsRes, statsRes, failedRes] = await Promise.all([
        axios.get('http://localhost:8080/api/admin/logs'),
        axios.get('http://localhost:8080/api/admin/stats'),
        axios.get('http://localhost:8080/api/admin/resumes/failed')
      ]);

      setLogs(logsRes.data);
      setStats(statsRes.data);
      setFailedParses(failedRes.data);
    } catch (error) {
      console.error('Error fetching admin data:', error);
    }
  };

  return (
    <div className="admin-panel">
      <h2>⚙️ Admin Panel</h2>
      
      <div className="admin-stats">
        <h3>📊 System Statistics</h3>
        <div className="stats-grid">
          <div className="stat-item">
            <strong>Total Processing Time:</strong> {stats.totalProcessingTime || 0}ms
          </div>
          <div className="stat-item">
            <strong>Average Processing Time:</strong> {stats.avgProcessingTime || 0}ms
          </div>
          <div className="stat-item">
            <strong>Success Rate:</strong> {stats.successRate || 0}%
          </div>
        </div>
      </div>

      <div className="admin-sections">
        <div className="logs-section">
          <h3>📝 Recent Parse Logs</h3>
          <div className="logs-table">
            <table>
              <thead>
                <tr>
                  <th>Time</th>
                  <th>Resume</th>
                  <th>Action</th>
                  <th>Status</th>
                  <th>Processing Time</th>
                </tr>
              </thead>
              <tbody>
                {logs.slice(0, 10).map(log => (
                  <tr key={log.id}>
                    <td>{new Date(log.createdAt).toLocaleString()}</td>
                    <td>{log.resumeId}</td>
                    <td>{log.action}</td>
                    <td>
                      <span className={`status ${log.status.toLowerCase()}`}>
                        {log.status}
                      </span>
                    </td>
                    <td>{log.processingTimeMs}ms</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="failed-section">
          <h3>❌ Failed Parse Attempts</h3>
          <div className="failed-list">
            {failedParses.map(item => (
              <div key={item.id} className="failed-item">
                <div className="failed-info">
                  <strong>{item.fileName}</strong>
                  <span className="error-message">{item.errorMessage}</span>
                </div>
                <div className="failed-time">
                  {new Date(item.uploadDate).toLocaleString()}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminPanel;
