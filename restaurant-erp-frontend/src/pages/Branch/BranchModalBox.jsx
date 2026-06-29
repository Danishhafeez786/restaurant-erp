import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function BranchModalBox({
  isOpen,
  onClose,
  mode,
  branch,
  onSuccess,
}) {


  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";


  const [organizations, setOrganizations] = useState([]);



  const [formData, setFormData] = useState({

    branchName: "",
    branchCode: "",
    address: "",
    city: "",
    phone: "",
    organization: null,
    isActive: true,

  });



  useEffect(() => {

    loadOrganizations();

  }, []);





  useEffect(() => {


    if (branch) {


      setFormData({

        ...branch,

      });


    } else {


      setFormData({

        branchName: "",
        branchCode: "",
        address: "",
        city: "",
        phone: "",
        organization: null,
        isActive: true,

      });


    }


  }, [branch]);





  const loadOrganizations = async () => {


    try {


      const response = await axiosClient.post(
        "/organization/search?page=0&size=100",
        {},
      );



      setOrganizations(response.data.data.content);



    } catch (error) {


      console.error(error);


    }


  };





  if (!isOpen) return null;





  const handleChange = (e) => {


    const { name, value, type, checked } = e.target;



    setFormData((prev)=>({

      ...prev,

      [name]: type === "checkbox" ? checked : value,

    }));


  };






  const handleSave = async () => {


    await axiosClient.post("/branch", formData);



    onSuccess();

    onClose();


  };






  const handleUpdate = async () => {



    await axiosClient.put(`/branch/${branch.id}`, formData);



    onSuccess();

    onClose();



  };






  return (



    <div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4 overflow-y-auto">


      <div className="bg-white rounded-2xl shadow-xl w-full max-w-5xl">


        <div className="border-b p-5 flex justify-between">


          <h2 className="text-xl font-bold">


            {isCreate && "Create Branch"}

            {isEdit && "Edit Branch"}

            {isView && "Branch Details"}



          </h2>




          <button onClick={onClose} className="text-xl">

            ✕

          </button>



        </div>






        <div className="p-6 grid md:grid-cols-2 gap-4">






          <div>


            <label className="block mb-2 text-sm font-medium text-gray-700">

              Branch Name

            </label>


            <input

              name="branchName"

              value={formData.branchName}

              onChange={handleChange}

              disabled={isView}

              placeholder="Enter Branch Name"

              className="w-full border rounded-lg px-4 py-3"

            />



          </div>







          <div>


            <label className="block mb-2 text-sm font-medium text-gray-700">

              Branch Code

            </label>



            <input


              name="branchCode"

              value={formData.branchCode}

              onChange={handleChange}

              disabled={isView}

              placeholder="Enter Branch Code"

              className="w-full border rounded-lg px-4 py-3"


            />


          </div>







          <div>


            <label className="block mb-2 text-sm font-medium text-gray-700">

              Address

            </label>


            <input


              name="address"

              value={formData.address}

              onChange={handleChange}

              disabled={isView}

              placeholder="Enter Address"

              className="w-full border rounded-lg px-4 py-3"



            />


          </div>







          <div>


            <label className="block mb-2 text-sm font-medium text-gray-700">

              City

            </label>


            <input


              name="city"

              value={formData.city}

              onChange={handleChange}

              disabled={isView}

              placeholder="Enter City"

              className="w-full border rounded-lg px-4 py-3"



            />


          </div>








          <div>


            <label className="block mb-2 text-sm font-medium text-gray-700">

              Phone

            </label>


            <input


              name="phone"

              value={formData.phone}

              onChange={handleChange}

              disabled={isView}

              placeholder="Enter Phone"

              className="w-full border rounded-lg px-4 py-3"



            />


          </div>








          <div>


            <label className="block mb-2 text-sm font-medium text-gray-700">

              Organization

            </label>



            <select


              disabled={isView}


              value={formData.organization?.id || ""}



              onChange={(e)=>{


                const selected = organizations.find(

                  (org)=>org.id === e.target.value

                );



                setFormData((prev)=>({

                  ...prev,

                  organization:selected,

                }));



              }}



              className="w-full border rounded-lg px-4 py-3"


            >


              <option value="">Select Organization</option>



              {organizations.map((org)=>(


                <option

                  key={org.id}

                  value={org.id}

                >

                  {org.organizationName}


                </option>



              ))}



            </select>



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



              Active Branch



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