import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function PermissionModalBox({
  isOpen,
  onClose,
  mode,
  permission,
  onSuccess,
}) {


  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";



  const [formData, setFormData] = useState({

    code: "",
    name: "",
    module: "",
    isActive: true,

  });






  useEffect(() => {


    if(permission){



      setFormData({

        ...permission,


      });



    }else{



      setFormData({


        code: "",

        name: "",

        module: "",

        isActive: true,


      });



    }



  },[permission]);








  if(!isOpen) return null;








  const handleChange = (e)=>{



    const {name,value,type,checked}=e.target;




    setFormData((prev)=>({



      ...prev,



      [name]: type==="checkbox" ? checked : value,



    }));



  };










  const handleSave = async()=>{



    await axiosClient.post("/permission",formData);



    onSuccess();

    onClose();



  };










  const handleUpdate = async()=>{



    await axiosClient.put(`/permission/${permission.id}`,formData);



    onSuccess();

    onClose();



  };












return (




<div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4 overflow-y-auto">



<div className="bg-white rounded-2xl shadow-xl w-full max-w-5xl">







<div className="border-b p-5 flex justify-between">





<h2 className="text-xl font-bold">



{isCreate && "Create Permission"}



{isEdit && "Edit Permission"}



{isView && "Permission Details"}




</h2>






<button onClick={onClose} className="text-xl">


✕


</button>






</div>









<div className="p-6 grid md:grid-cols-2 gap-4">







<div>



<label className="block mb-2 text-sm font-medium text-gray-700">


Code


</label>





<input



name="code"



value={formData.code}



onChange={handleChange}



disabled={isView}



placeholder="Enter Permission Code"



className="w-full border rounded-lg px-4 py-3"



/>






</div>









<div>



<label className="block mb-2 text-sm font-medium text-gray-700">


Name


</label>





<input



name="name"



value={formData.name}



onChange={handleChange}



disabled={isView}



placeholder="Enter Permission Name"



className="w-full border rounded-lg px-4 py-3"



/>






</div>









<div>



<label className="block mb-2 text-sm font-medium text-gray-700">


Module


</label>





<input



name="module"



value={formData.module}



onChange={handleChange}



disabled={isView}



placeholder="Enter Module"



className="w-full border rounded-lg px-4 py-3"



/>






</div>









<div className="flex items-center mt-8">



<label className="flex items-center gap-3 text-sm font-medium text-gray-700">





<input



type="checkbox"



name="isActive"



checked={formData.isActive}



onChange={handleChange}



disabled={isView}



className="h-4 w-4"



/>






Active Permission






</label>





</div>









</div>









<div className="border-t p-5 flex justify-end gap-3">






<button



onClick={onClose}



className="border px-5 py-2 rounded-lg"



>


Close


</button>









{isCreate && (




<button



onClick={handleSave}



className="bg-green-600 text-white px-5 py-2 rounded-lg"



>


Save


</button>




)}









{isEdit && (




<button



onClick={handleUpdate}



className="bg-blue-600 text-white px-5 py-2 rounded-lg"



>


Update


</button>




)}








</div>







</div>





</div>






);



}