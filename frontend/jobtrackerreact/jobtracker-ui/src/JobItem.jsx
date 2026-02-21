import "./JobItem.css";

import { useState } from "react";

function JobItem({ job, updateJob, deleteJob }) {

  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState(job);

  const handleEditChange = (e) => {
    setEditData({
      ...editData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="job-card">

      {isEditing ? (
        <>
          <input name="companyName" value={editData.companyName} onChange={handleEditChange}/>
          <input name="role" value={editData.role} onChange={handleEditChange}/>

          <select name="status" value={editData.status} onChange={handleEditChange}>
            <option>Applied</option>
            <option>Selected</option>
            <option>Rejected</option>
          </select>

          <input type="date" name="appliedDate"
            value={editData.appliedDate||""} onChange={handleEditChange}/>

          <input type="date" name="interviewDate"
            value={editData.interviewDate||""} onChange={handleEditChange}/>

          <input name="jobLink" value={editData.jobLink} onChange={handleEditChange}/>
          <input name="notes" value={editData.notes} onChange={handleEditChange}/>

          <button onClick={()=>{
            updateJob(editData);
            setIsEditing(false);
          }}>
            Save
          </button>
        </>
      ) : (
        <>
          <p><b>Company:</b> {job.companyName}</p>
          <p><b>Role:</b> {job.role}</p>

          <p>
            <b>Status:</b>{" "}
            <span className={`status-${(job.status || "applied").toLowerCase()}`}>

              {job.status || "Applied"}
            </span>
          </p>

          <p><b>Applied:</b> {job.appliedDate}</p>
          <p><b>Interview:</b> {job.interviewDate}</p>
          
          <p><b>Link:</b> {job.jobLink}</p>
          <p><b>Notes:</b> {job.notes}</p>

          <button onClick={()=>setIsEditing(true)}>Edit</button>
          <button className="delete-btn" onClick={()=>deleteJob("Rejected")}>
            Clear Rejected
          </button>
        </>
      )}

    </div>
  );
}

export default JobItem;
