import { useState } from "react";
import Sidebar from "../../components/Sidebar";

export default function CreateEmployee() {

  const [image, setImage] = useState(null);


  // Later replace this from logged-in user/JWT
  const currentUserRole = "SUPER_ADMIN";


  const allRoles = [
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



  const getAllowedRoles = () => {


    if(currentUserRole === "OWNER"){
      return allRoles;
    }


    if(currentUserRole === "SUPER_ADMIN"){

      return allRoles.filter(
        role =>
        ![
          "OWNER",
          "SUPER_ADMIN",
        ].includes(role)
      );

    }


    if(currentUserRole === "MANAGER"){

      return allRoles.filter(
        role =>
        ![
          "OWNER",
          "SUPER_ADMIN",
          "MANAGER"
        ].includes(role)
      );

    }


    return allRoles;

  };


  const availableRoles = getAllowedRoles();





  const [formData,setFormData]=useState({

    firstName:"",
    lastName:"",
    fatherName:"",
    cnic:"",
    dob:"",
    gender:"",

    phone:"",
    alternatePhone:"",
    emergencyContactName:"",
    emergencyContactNumber:"",

    address:"",
    city:"",
    state:"",
    country:"",
    postalCode:"",

    joiningDate:"",
    salary:"",
    status:"",
    employmentType:"",
    role:"",

    description:""

  });





  const requiredFields=[

    "firstName",
    "lastName",
    "fatherName",
    "cnic",
    "dob",
    "gender",
    "phone",
    "joiningDate",
    "salary",
    "status",
    "employmentType",
    "role"

  ];




  const isFormValid =
  requiredFields.every(
    (field) => formData[field].trim() !== ""
  )
  &&
  image !== null;





  const handleChange=(name,value)=>{

    setFormData(prev=>({

      ...prev,
      [name]:value

    }));

  };





  const handleSubmit=(e)=>{

    e.preventDefault();

    console.log({

      ...formData,
      image

    });


    alert("Employee Saved Successfully");

  };






return (

<div className="min-h-screen bg-gray-100 lg:flex">


<Sidebar/>



<div className="flex-1 flex flex-col min-h-screen overflow-y-auto p-4 md:p-6 bg-slate-100">



<h1 className="text-3xl font-bold mb-6">

Add New Employee

</h1>




<form 
onSubmit={handleSubmit}
className="space-y-5"
>



{/* PERSONAL */}

<div className="grid lg:grid-cols-4 gap-5">



<div className="lg:col-span-3 bg-white rounded-xl shadow p-5">


<h2 className="font-semibold text-lg mb-5">

Core Personal & Profile

</h2>



<div className="grid md:grid-cols-2 xl:grid-cols-4 gap-4">



<Input
label="First Name"
name="firstName"
value={formData.firstName}
onChange={handleChange}

/>



<Input
label="Last Name"
name="lastName"
value={formData.lastName}
onChange={handleChange}
/>



<Input
label="Father Name"
name="fatherName"
value={formData.fatherName}
onChange={handleChange}
/>



<Input
label="CNIC"
name="cnic"
value={formData.cnic}
onChange={handleChange}
/>



<Input
label="Date Of Birth"
type="date"
name="dob"
value={formData.dob}
onChange={handleChange}
/>



<Select

label="Gender"

name="gender"

value={formData.gender}

onChange={handleChange}

options={[
"MALE",
"FEMALE",
"OTHER"
]}

/>



</div>


</div>





{/* IMAGE */}


<div className="bg-white rounded-xl shadow p-5">


<div className="flex flex-col items-center justify-center h-full">


<label className="cursor-pointer">


<div className="w-32 h-32 rounded-full border-2 border-dashed flex items-center justify-center text-4xl">

+

</div>


<input

type="file"

hidden

onChange={(e)=>{

  const file = e.target.files[0];

  if(file){
    setImage(URL.createObjectURL(file));
  }

}}

/>


</label>



<p className="mt-4 font-medium">
Upload Profile Image <span className="text-red-500">*</span>
</p>


{!image && (
  <p className="text-red-500 text-sm mt-2">
    Profile image is required
  </p>
)}



{image &&

<img

src={image}

className="w-24 h-24 rounded-full mt-4 object-cover"

/>

}



</div>



</div>




</div>





{/* CONTACT */}



<div className="bg-white rounded-xl shadow p-5">


<h2 className="font-semibold text-lg mb-5">

Contact & Address Details

</h2>



<div className="grid md:grid-cols-2 xl:grid-cols-4 gap-4">


<Input label="Phone Number" name="phone" value={formData.phone} onChange={handleChange}/>


<Input label="Alternate Phone Number" name="alternatePhone" value={formData.alternatePhone} onChange={handleChange}/>


<Input label="Emergency Contact Name" name="emergencyContactName" value={formData.emergencyContactName} onChange={handleChange}/>


<Input label="Emergency Contact Number" name="emergencyContactNumber" value={formData.emergencyContactNumber} onChange={handleChange}/>


<Input label="Address" name="address" value={formData.address} onChange={handleChange}/>


<Input label="City" name="city" value={formData.city} onChange={handleChange}/>


<Input label="State" name="state" value={formData.state} onChange={handleChange}/>


<Input label="Country" name="country" value={formData.country} onChange={handleChange}/>


<Input label="Postal Code" name="postalCode" value={formData.postalCode} onChange={handleChange}/>


</div>



</div>






{/* EMPLOYMENT */}



<div className="grid lg:grid-cols-2 gap-5">


<div className="bg-white rounded-xl shadow p-5">


<h2 className="font-semibold text-lg mb-5">

Employment & Role Settings

</h2>



<div className="grid md:grid-cols-2 gap-4">



<Input
label="Joining Date"
type="date"
name="joiningDate"
value={formData.joiningDate}
onChange={handleChange}
/>



<Input
label="Basic Salary"
name="salary"
value={formData.salary}
onChange={handleChange}
/>



<Select
label="Employee Status"
name="status"
value={formData.status}
onChange={handleChange}

options={[
"ACTIVE",
"INACTIVE",
"PROBATION",
"SUSPENDED",
"RESIGNED",
"TERMINATED"
]}

/>



<Select

label="Employment Type"

name="employmentType"

value={formData.employmentType}

onChange={handleChange}

options={[
"FULL_TIME",
"PART_TIME",
"CONTRACT",
"TEMPORARY",
"INTERN"
]}

/>



<Select

label="Department / Role"

name="role"

value={formData.role}

onChange={handleChange}

options={availableRoles}

/>



</div>


</div>





<div className="bg-white rounded-xl shadow p-5">


<h2 className="font-semibold text-lg mb-5">

Additional Details & Description

</h2>



<textarea

rows="8"

value={formData.description}

onChange={(e)=>
handleChange("description",e.target.value)
}

className="w-full border rounded-lg p-3"

/>



</div>


</div>





<div className="flex justify-end gap-3">

  <button type="button" className="px-6 py-2 border rounded-lg"> Cancel </button>


<button

disabled={!isFormValid}

className={`px-6 py-2 rounded-lg text-white ${
isFormValid
?
"bg-[#0d4039] hover:bg-green-700"
:
"bg-gray-400 cursor-not-allowed"
}`}

>

Save Employee

</button>



</div>




</form>


</div>


</div>

);

}







function Input({
label,
type="text",
name,
value,
onChange
}){


return (

<div>

<label className="block text-sm font-medium mb-1">

{label}

</label>


<input

type={type}

value={value}

onChange={(e)=>onChange(name,e.target.value)}

className="w-full border rounded-lg px-3 py-2"

/>


</div>


)

}





function Select({
label,
name,
value,
onChange,
options
}){


return (

<div>


<label className="block text-sm font-medium mb-1">

{label}

</label>



<select

value={value}

onChange={(e)=>onChange(name,e.target.value)}

className="w-full border rounded-lg px-3 py-2"

>


<option value="">

Select

</option>



{options.map(item=>(

<option key={item} value={item}>

{item}

</option>

))}



</select>


</div>


)

}