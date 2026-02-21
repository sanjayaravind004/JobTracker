import "./JobForm.css";

function JobForm({ formData, handleChange, addJob }) {
  return (
    <div className="job-form">

      <h2>Add Job</h2>

      <input name="companyName" placeholder="Company"
        value={formData.companyName} onChange={handleChange} />

      <input name="role" placeholder="Role"
        value={formData.role} onChange={handleChange} />

      {/* <div className="status-group">
        <label>
          <input type="radio" name="status" value="Applied"
            checked={formData.status==="Applied"} onChange={handleChange}/>
          Applied
        </label>

        <label>
          <input type="radio" name="status" value="Selected"
            checked={formData.status==="Selected"} onChange={handleChange}/>
          Selected
        </label>

        <label>
          <input type="radio" name="status" value="Rejected"
            checked={formData.status==="Rejected"} onChange={handleChange}/>
          Rejected
        </label>
      </div> */}

     <label className="date-label">Applied Date</label>
<input
  type="date"
  name="appliedDate"
  value={formData.appliedDate}
  onChange={handleChange}
/>

<label className="date-label">Interview Date</label>
<input
  type="date"
  name="interviewDate"
  value={formData.interviewDate}
  onChange={handleChange}
/>

      <input name="jobLink" placeholder="Job Link"
        value={formData.jobLink} onChange={handleChange}/>

      <input name="notes" placeholder="Notes"
        value={formData.notes} onChange={handleChange}/>

      <button onClick={addJob}>Add Job</button>
    </div>
  );
}

export default JobForm;
