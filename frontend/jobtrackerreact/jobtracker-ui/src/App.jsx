import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Routes, Route, Link } from "react-router-dom";
import JobForm from "./JobForm";
import JobItem from "./JobItem";
import "./App.css";

function App() {

  const navigate = useNavigate();

  const [search, setSearch] = useState("");

  const [jobs, setJobs] = useState([]);

  const [formData, setFormData] = useState({
    companyName:"",
    role:"",
    status:"",
    appliedDate:"",
    interviewDate:"",
    jobLink:"",
    notes:""
  });

  useEffect(() => {
    fetch("https://jobtracker-dk6a.onrender.com/job/all")
      .then(res => res.json())
      .then(data => setJobs(data));
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const addJob = () => {

    //for validation
    if(!formData.companyName.trim() || !formData.role.trim() ){
    alert("Please fill all mandatory fields");
  return;
}
    fetch("https://jobtracker-dk6a.onrender.com/job/add",{
      method:"POST",
      headers:{ "Content-Type":"application/json"},
      body:JSON.stringify(formData)
    })
      .then(res => res.json())
      .then(newJob => setJobs([...jobs,newJob]));
      navigate("/jobs"); //auto navigate
  };

  const deleteJob = (status) => {
    fetch(`https://jobtracker-dk6a.onrender.com/job/clear/${status}`,{
      method:"DELETE"
    }).then(()=> {
      setJobs(jobs.filter(j => j.status !== status));
    });
  };

  const updateJob = (job) => {
    fetch(`https://jobtracker-dk6a.onrender.com/job/update/${job.id}`,{
      method:"PUT",
      headers:{ "Content-Type":"application/json"},
      body:JSON.stringify(job)
    })
      .then(res => res.json())
      .then(updated => {
        setJobs(jobs.map(j => j.id === updated.id ? updated : j));
      });
  };

  return (
    <div className="app-container">

      {/* HEADER NAVIGATION */}
      <div className="header-bar">
        <h1>Job Tracker</h1>

        <div>
          <Link to="/jobs">
            <button>Jobs</button>
            <input
  placeholder="Search company or role..."
  value={search}
  onChange={(e)=>setSearch(e.target.value)}
/>

          </Link>

          <Link to="/add">
            <button>+ Add Job</button>
          </Link>
        </div>
      </div>

      {/* ROUTES */}
      <Routes>

        {/* JOB LIST PAGE */}
        <Route
          path="/jobs"
          element={
            <>
              {jobs.filter(job =>
    job.companyName.toLowerCase().includes(search.toLowerCase()) ||
    job.role.toLowerCase().includes(search.toLowerCase())
  )
              .map(job => (
                <JobItem
                  key={job.id}
                  job={job}
                  updateJob={updateJob}
                  deleteJob={deleteJob}
                />
              ))}
            </>
          }
        />

        {/* ADD JOB PAGE */}
        <Route
          path="/add"
          element={
            <JobForm
              formData={formData}
              handleChange={handleChange}
              addJob={addJob}
            />
          }
        />

      </Routes>

    </div>
  );
}

export default App;
