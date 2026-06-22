import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function EmployeeTable() {

  const navigate = useNavigate();


  const [employees] = useState([
    {
      id: 1,
      name: "Ali Khan",
      role: "CHEF",
      phone: "03001234567",
      status: "ACTIVE",
      salary: "50000",
    },
    {
      id: 2,
      name: "Ahmed Raza",
      role: "CASHIER",
      phone: "03111234567",
      status: "PROBATION",
      salary: "35000",
    },
    {
      id: 3,
      name: "Usman Tariq",
      role: "WAITER",
      phone: "03211234567",
      status: "RESIGNED",
      salary: "30000",
    },
    {
      id:4,
      name:"Hassan Ali",
      role:"MANAGER",
      phone:"03331234567",
      status:"SUSPENDED",
      salary:"80000",
    },
    {
      id:4,
      name:"Abrar Ahmed",
      role:"BAKER",
      phone:"03123461891",
      status:"INACTIVE",
      salary:"18000",
    },
    {
      id:4,
      name:"Farhan Shah",
      role:"PIZZA_MAKER",
      phone:"03052384637",
      status:"TERMINATED",
      salary:"20000",
    }


  ]);



  // SEARCH FILTER STATES

  const [search,setSearch] = useState("");

  const [roleFilter,setRoleFilter] = useState("");

  const [statusFilter,setStatusFilter] = useState("");



  // PAGINATION

  const [currentPage,setCurrentPage] = useState(1);

  const recordsPerPage = 20;




  const roles = [
    "OWNER",
    "SUPER_ADMIN",
    "MANAGER",
    "CASHIER",
    "WAITER",
    "HOST",
    "RECEPTIONIST",
    "HEAD_CHEF",
    "SOUS_CHEF",
    "CHEF",
    "COOK",
    "BAKER",
    "BARISTA",
    "PIZZA_MAKER",
    "GRILL_CHEF",
    "PASTRY_CHEF",
    "KITCHEN_ASSISTANT",
    "KITCHEN_HELPER",
    "DISHWASHER",
    "INVENTORY_MANAGER",
    "STORE_KEEPER",
    "PROCUREMENT_OFFICER",
    "PURCHASE_OFFICER",
    "ACCOUNTANT",
    "FINANCE_OFFICER",
    "DELIVERY_RIDER",
    "DELIVERY_MANAGER",
    "DISPATCHER",
    "CLEANER",
    "JANITOR",
    "SECURITY_GUARD",
    "MAINTENANCE_TECHNICIAN"
  ];



  const statuses = [
    "ACTIVE",
    "INACTIVE",
    "PROBATION",
    "SUSPENDED",
    "RESIGNED",
    "TERMINATED"
  ];





  // FILTER LOGIC

  const filteredEmployees = employees.filter((emp)=>{


    return (

      emp.name
      .toLowerCase()
      .includes(search.toLowerCase())


      &&


      (
        roleFilter === "" ||
        emp.role === roleFilter
      )


      &&


      (
        statusFilter === "" ||
        emp.status === statusFilter
      )

    );

  });




  // PAGINATION LOGIC


  const lastIndex =
  currentPage * recordsPerPage;


  const firstIndex =
  lastIndex - recordsPerPage;



  const currentEmployees =
  filteredEmployees.slice(
    firstIndex,
    lastIndex
  );



  const totalPages =
  Math.ceil(
    filteredEmployees.length /
    recordsPerPage
  );





  const getStatusColor = (status) => {

    switch(status){

      case "ACTIVE":
        return "bg-green-100 text-green-700";


      case "INACTIVE":
        return "bg-yellow-100 text-yellow-700";  


      case "PROBATION":
        return "bg-blue-100 text-blue-700";


      case "SUSPENDED":
        return "bg-red-100 text-red-700";


      case "RESIGNED":
        return "bg-gray-100 text-gray-700";


      case "TERMINATED":
        return "bg-red-100 text-red-700";


      default:
        return "bg-gray-100 text-gray-700";

    }

  };






  return (

<div className="bg-white rounded-2xl shadow-md p-4 md:p-6">



{/* HEADER */}

<div className="flex items-center justify-between mb-4">

<h2 className="text-2xl font-bold text-gray-800">

Employees Record

</h2>



<button

onClick={() => navigate("/create-employee")}

className="px-6 py-2 bg-[#0d4039] text-white rounded-lg hover:bg-green-700"

>

+ Add Employee

</button>


</div>







{/* SEARCH + FILTER */}


<div className="grid md:grid-cols-3 gap-4 mb-5">


<input

type="text"

placeholder="Search Employee..."

value={search}

onChange={(e)=>{

setSearch(e.target.value);

setCurrentPage(1);

}}

className="border rounded-lg px-4 py-2"

/>




<select

value={roleFilter}

onChange={(e)=>{

setRoleFilter(e.target.value);

setCurrentPage(1);

}}

className="border rounded-lg px-4 py-2"

>


<option value="">

Filter By Role

</option>


{

roles.map(role=>(

<option key={role}>

{role}

</option>

))

}


</select>





<select

value={statusFilter}

onChange={(e)=>{

setStatusFilter(e.target.value);

setCurrentPage(1);

}}

className="border rounded-lg px-4 py-2"

>


<option value="">

Filter By Status

</option>


{

statuses.map(status=>(

<option key={status}>

{status}

</option>

))

}


</select>


</div>










{/* DESKTOP TABLE */}



<div className="hidden md:block overflow-x-auto">


<table className="w-full text-left min-w-[700px]">


<thead>

<tr className="border-b text-gray-500">


<th className="py-3">

Name

</th>


<th>

Role

</th>


<th>

Phone

</th>


<th>

Salary

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


{

currentEmployees.map((emp)=>(


<tr

key={emp.id}

className="border-b hover:bg-gray-50 transition"

>


<td className="py-3 font-semibold">

{emp.name}

</td>



<td>

{emp.role}

</td>



<td>

{emp.phone}

</td>



<td>

Rs. {emp.salary}

</td>




<td>


<span

className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(emp.status)}`}

>

{emp.status}

</span>


</td>





<td>


<button className="text-green-600 mr-3">

View

</button>


<button className="text-blue-600 mr-3">

Edit

</button>


<button className="text-red-600">

Delete

</button>


</td>



</tr>



))


}



</tbody>



</table>


</div>









{/* MOBILE CARDS */}



<div className="md:hidden space-y-4">


{

currentEmployees.map((emp)=>(


<div

key={emp.id}

className="border rounded-2xl p-4 shadow-sm"

>



<div className="flex justify-between items-center">


<h3 className="font-bold text-lg">

{emp.name}

</h3>



<span

className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(emp.status)}`}

>

{emp.status}

</span>


</div>





<div className="mt-3 text-sm text-gray-600 space-y-1">


<p>

<b>Role:</b> {emp.role}

</p>


<p>

<b>Phone:</b> {emp.phone}

</p>


<p>

<b>Salary:</b> Rs. {emp.salary}

</p>


</div>




<div className="flex gap-3 mt-4">


<button className="flex-1 bg-green-500 text-white py-2 rounded-xl">

View

</button>



<button className="flex-1 bg-blue-500 text-white py-2 rounded-xl">

Edit

</button>



<button className="flex-1 bg-red-500 text-white py-2 rounded-xl">

Delete

</button>



</div>



</div>



))


}


</div>









{/* PAGINATION */}



<div className="flex justify-center gap-2 mt-5">


{

Array.from(
{length:totalPages},
(_,index)=>(


<button

key={index}

onClick={()=>setCurrentPage(index+1)}

className={`px-4 py-2 rounded-lg ${
currentPage===index+1
?
"bg-[#0d4039] text-white"
:
"bg-gray-200"
}`}

>


{index+1}


</button>


)

)

}


</div>






</div>


  );

}