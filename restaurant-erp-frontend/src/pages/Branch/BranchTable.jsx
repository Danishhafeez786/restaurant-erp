import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

import BranchModalBox from "./BranchModalBox";

export default function BranchTable() {
  const [branches, setBranches] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");

  const [selectedBranch, setSelectedBranch] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [pageSize, setPageSize] = useState(10);

  const [sortBy, setSortBy] = useState("createdAt");

  const [direction, setDirection] = useState("DESC");

  const [searchCriteria, setSearchCriteria] = useState({
    branchName: "",
    branchCode: "",
    city: "",
    phone: "",
    organization: "",
    isActive: "",
  });


  // ===== LOAD DATA =====
  const loadBranches = async () => {
    try {
      setLoading(true);

      const payload = {
        branchName: searchCriteria.branchName || null,
        branchCode: searchCriteria.branchCode || null,
        city: searchCriteria.city || null,
        phone: searchCriteria.phone || null,
        organization: searchCriteria.organization || null,

        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };


      const response = await axiosClient.post(
        `/branch/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload,
      );


      const pageData = response.data.data;


      setBranches(pageData.content);
      setTotalPages(pageData.totalPages);


    } catch (error) {
      console.error(error);

    } finally {

      setLoading(false);

    }
  };


  // ===== INIT LOAD =====
  useEffect(() => {
    loadBranches();

  }, [currentPage, pageSize, sortBy, direction]);



  useEffect(() => {

    console.log("Connecting to SSE...");


    const eventSource = new EventSource(
      "http://localhost:8080/api/branch/stream",
    );


    return () => eventSource.close();


  }, []);




  // ===== DELETE =====
  const handleDelete = async (id) => {

    if (!window.confirm("Delete this branch?")) return;


    try {

      await axiosClient.delete(`/branch/${id}`);


      alert("Branch Deleted Successfully");


      loadBranches();


    } catch (error) {

      console.error(error);

    }

  };



  // ===== RESTORE =====
  const handleRestore = async (id) => {


    if (!window.confirm("Restore this branch?")) return;



    try {


      await axiosClient.patch(`/branch/${id}/restore`);



      alert("Branch Restored Successfully");


      loadBranches();



    } catch (error) {


      console.error(error);


    }

  };



  const getStatusColor = (status) =>
    status
      ? "bg-green-100 text-green-700"
      : "bg-red-100 text-red-700";




  return (

    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">


      {/* HEADER */}

      <div className="flex justify-between items-center mb-5">


        <h2 className="text-2xl font-bold">
          Branches
        </h2>



        <button

          onClick={() => {

            setModalMode("create");

            setSelectedBranch(null);

            setShowModal(true);

          }}

          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"

        >

          + Add Branch

        </button>


      </div>





      {/* FILTERS */}


      <div className="bg-white border rounded-xl shadow-sm p-4 mb-6">


        <div className="flex flex-wrap items-center gap-3">



          <input

            type="text"

            placeholder="Branch Name"

            value={searchCriteria.branchName}

            onChange={(e)=>

              setSearchCriteria({

                ...searchCriteria,

                branchName:e.target.value

              })

            }

            className="h-10 w-52 rounded-lg border px-3 text-sm"

          />




          <input

            type="text"

            placeholder="Branch Code"

            value={searchCriteria.branchCode}

            onChange={(e)=>

              setSearchCriteria({

                ...searchCriteria,

                branchCode:e.target.value

              })

            }

            className="h-10 w-44 rounded-lg border px-3 text-sm"

          />





          <input

            type="text"

            placeholder="City"

            value={searchCriteria.city}

            onChange={(e)=>

              setSearchCriteria({

                ...searchCriteria,

                city:e.target.value

              })

            }

            className="h-10 w-36 rounded-lg border px-3 text-sm"

          />





          <input

            type="text"

            placeholder="Phone"

            value={searchCriteria.phone}

            onChange={(e)=>

              setSearchCriteria({

                ...searchCriteria,

                phone:e.target.value

              })

            }

            className="h-10 w-36 rounded-lg border px-3 text-sm"

          />





          <select

            value={searchCriteria.isActive}

            onChange={(e)=>

              setSearchCriteria({

                ...searchCriteria,

                isActive:e.target.value

              })

            }

            className="h-10 rounded-lg border px-3 text-sm"

          >

            <option value="">Status</option>

            <option value="true">Active</option>

            <option value="false">Inactive</option>


          </select>





          <select

            value={sortBy}

            onChange={(e)=>setSortBy(e.target.value)}

            className="h-10 rounded-lg border px-3 text-sm"

          >

            <option value="createdAt">
              Created
            </option>

            <option value="branchName">
              Branch Name
            </option>


            <option value="city">
              City
            </option>


          </select>





          <select

            value={direction}

            onChange={(e)=>setDirection(e.target.value)}

            className="h-10 rounded-lg border px-3 text-sm"

          >

            <option value="DESC">
              Newest
            </option>

            <option value="ASC">
              Oldest
            </option>


          </select>





          <select

            value={pageSize}

            onChange={(e)=>{

              setPageSize(Number(e.target.value));

              setCurrentPage(0);

            }}

            className="h-10 rounded-lg border px-3 text-sm"

          >

            <option value={10}>10</option>

            <option value={20}>20</option>

            <option value={50}>50</option>

            <option value={100}>100</option>


          </select>






          <button

            onClick={()=>{

              setCurrentPage(0);

              loadBranches();

            }}

            className="h-10 px-5 rounded-lg bg-[#0d4039] text-white"

          >

            Search

          </button>
            {/* Reset */}
          <button
            onClick={() => {
              setSearchCriteria({
                branchName: "",
                branchCode: "",
                city: "",
                Phone: "",
                isActive: "",
                
              });

              setCurrentPage(0);
              setPageSize(10);
              setSortBy("createdAt");
              setDirection("DESC");

              loadPlans();
            }}
            className="h-10 px-5 rounded-lg border hover:bg-gray-100 transition"
          >
            Reset
          </button>




        </div>


      </div>







      {/* TABLE */}

      <div className="hidden md:block overflow-x-auto">


        <table className="w-full">


          <thead>


            <tr className="border-b text-left">


              <th className="py-3">
                Branch Name
              </th>

              <th>
                Branch Code
              </th>


              <th>
                Address
              </th>


              <th>
                City
              </th>


              <th>
                Phone
              </th>


              <th>
                Organization
              </th>


              <th>
                Status
              </th>


              <th>
                Action
              </th>


            </tr>


          </thead>





          <tbody>


            {branches.map((branch)=>(


              <tr
                key={branch.id}
                className="border-b hover:bg-gray-50"
              >



                <td className="py-3">
                  {branch.branchName}
                </td>


                <td>
                  {branch.branchCode}
                </td>


                <td>
                  {branch.address}
                </td>


                <td>
                  {branch.city}
                </td>


                <td>
                  {branch.phone}
                </td>


                <td>
                  {branch.organization?.organizationName || "N/A"}
                </td>



                <td>


                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(branch.isActive)}`}
                  >

                    {branch.isActive ? "ACTIVE" : "INACTIVE"}

                  </span>


                </td>





                <td>


                  <button

                    onClick={()=>{

                      setModalMode("view");

                      setSelectedBranch(branch);

                      setShowModal(true);

                    }}

                    className="text-green-600 mr-3"

                  >

                    View

                  </button>





                  <button

                    onClick={()=>{

                      setModalMode("edit");

                      setSelectedBranch(branch);

                      setShowModal(true);

                    }}

                    className="text-blue-600 mr-3"

                  >

                    Edit

                  </button>





                  <button

                    onClick={()=>handleDelete(branch.id)}

                    className="text-red-600"

                  >

                    Delete

                  </button>



                </td>



              </tr>


            ))}


          </tbody>



        </table>


      </div>







      {/* PAGINATION */}


      <div className="flex justify-center gap-2 mt-5">


        {[...Array(totalPages)].map((_,i)=>(


          <button

            key={i}

            onClick={()=>setCurrentPage(i)}

            className={`px-3 py-1 rounded ${
              currentPage===i
              ?"bg-green-600 text-white"
              :"bg-gray-200"
            }`}

          >

            {i+1}

          </button>


        ))}


      </div>







      {/* MODAL */}


      <BranchModalBox

        isOpen={showModal}

        onClose={()=>setShowModal(false)}

        mode={modalMode}

        branch={selectedBranch}

        onSuccess={loadBranches}

      />


    </div>


  );

}