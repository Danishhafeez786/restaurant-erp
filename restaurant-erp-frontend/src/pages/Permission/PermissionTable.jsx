import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

import PermissionModalBox from "./PermissionModalBox";

export default function PermissionTable() {

  const [permissions, setPermissions] = useState([]);

  const [loading, setLoading] = useState(false);


  const [showModal, setShowModal] = useState(false);

  const [modalMode, setModalMode] = useState("create");


  const [selectedPermission, setSelectedPermission] = useState(null);


  const [currentPage, setCurrentPage] = useState(0);

  const [totalPages, setTotalPages] = useState(0);


  const [pageSize, setPageSize] = useState(10);


  const [sortBy, setSortBy] = useState("createdAt");


  const [direction, setDirection] = useState("DESC");



  const [searchCriteria, setSearchCriteria] = useState({

    code: "",

    name: "",

    module: "",

    isActive: "",

  });







  // ===== LOAD DATA =====

  const loadPermissions = async () => {


    try {


      setLoading(true);



      const payload = {


        code: searchCriteria.code || null,

        name: searchCriteria.name || null,

        module: searchCriteria.module || null,


        isActive:

          searchCriteria.isActive === ""

            ? null

            : searchCriteria.isActive === "true",


      };





      const response = await axiosClient.post(


        `/permission/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,

        payload,


      );






      const pageData = response.data.data;



      setPermissions(pageData.content);


      setTotalPages(pageData.totalPages);




    } catch(error) {


      console.error(error);



    } finally {



      setLoading(false);



    }


  };







  // ===== INIT LOAD =====


  useEffect(() => {


    loadPermissions();



  }, [currentPage,pageSize,sortBy,direction]);









  useEffect(() => {


    console.log("Connecting to SSE...");



    const eventSource = new EventSource(


      "http://localhost:8080/api/permission/stream",


    );



    return () => eventSource.close();



  }, []);










  // ===== DELETE =====


  const handleDelete = async(id)=>{


    if(!window.confirm("Delete this permission?")) return;



    try{


      await axiosClient.delete(`/permission/${id}`);



      alert("Permission Deleted Successfully");



      loadPermissions();



    }catch(error){


      console.error(error);


    }


  };










  // ===== RESTORE =====


  const handleRestore = async(id)=>{



    if(!window.confirm("Restore this permission?")) return;




    try{



      await axiosClient.patch(`/permission/${id}/restore`);




      alert("Permission Restored Successfully");




      loadPermissions();





    }catch(error){



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

Permissions

</h2>







<button


onClick={()=>{


setModalMode("create");


setSelectedPermission(null);


setShowModal(true);



}}


className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"



>


+ Add Permission


</button>




</div>












{/* FILTERS */}



<div className="bg-white border rounded-xl shadow-sm p-4 mb-6">



<div className="flex flex-wrap items-center gap-3">






<input


type="text"


placeholder="Code"


value={searchCriteria.code}



onChange={(e)=>

setSearchCriteria({

...searchCriteria,

code:e.target.value

})

}



className="h-10 w-44 rounded-lg border px-3 text-sm"



/>









<input


type="text"


placeholder="Name"



value={searchCriteria.name}



onChange={(e)=>

setSearchCriteria({

...searchCriteria,

name:e.target.value

})

}



className="h-10 w-52 rounded-lg border px-3 text-sm"



/>









<input


type="text"


placeholder="Module"



value={searchCriteria.module}



onChange={(e)=>

setSearchCriteria({

...searchCriteria,

module:e.target.value

})

}



className="h-10 w-44 rounded-lg border px-3 text-sm"



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



<option value="name">

Name

</option>



<option value="module">

Module

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


loadPermissions();



}}



className="h-10 px-5 rounded-lg bg-[#0d4039] text-white"



>


Search


</button>

{/* Reset */}
        <button
            onClick={() => {
                setSearchCriteria({
                    code: "",
                    name: "",
                    module: "",
                    isActive: "",
                });

                setSortBy("createdAt");
                setDirection("DESC");
                setPageSize(10);
                setCurrentPage(0);

                loadOrganizations();
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

Code

</th>



<th>

Name

</th>



<th>

Module

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



{permissions.map((permission)=>(




<tr key={permission.id} className="border-b hover:bg-gray-50">





<td className="py-3">

{permission.code}

</td>





<td>

{permission.name}

</td>





<td>

{permission.module}

</td>







<td>


<span


className={`px-3 py-1 rounded-full text-sm ${getStatusColor(permission.isActive)}`}



>



{permission.isActive ? "ACTIVE":"INACTIVE"}



</span>


</td>









<td>



<button


onClick={()=>{


setModalMode("view");


setSelectedPermission(permission);


setShowModal(true);



}}



className="text-green-600 mr-3"



>


View


</button>








<button


onClick={()=>{


setModalMode("edit");


setSelectedPermission(permission);


setShowModal(true);



}}



className="text-blue-600 mr-3"



>


Edit


</button>








<button


onClick={()=>handleDelete(permission.id)}



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



<PermissionModalBox


isOpen={showModal}



onClose={()=>setShowModal(false)}



mode={modalMode}



permission={selectedPermission}



onSuccess={loadPermissions}



/>







</div>




);



}