import Sidebar from "../components/Sidebar";

const Dashboard = () => {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
        <Sidebar />

        <div className="flex-1 overflow-y-auto">
        {/* HEADER */}
        <div className="bg-white px-8 py-6 shadow-sm flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-800">
              Dashboard
            </h1>

            <p className="text-gray-500 mt-1">
              Welcome back to your restaurant management system
            </p>
          </div>

          <div className="bg-green-100 text-green-700 px-5 py-2 rounded-xl text-sm font-semibold">
            Restaurant Online
          </div>
        </div>

        {/* CONTENT */}
        <div className="p-8">
          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6">
            <StatCard
              title="Today's Orders"
              value="145"
            />

            <StatCard
              title="Today's Sales"
              value="Rs. 125,000"
            />

            <StatCard
              title="Reserved Tables"
              value="18"
            />

            <StatCard
              title="Delivery Orders"
              value="42"
            />
          </div>
        </div>

        {/* MAIN CONTENT */}
      <div className="flex-1 overflow-y-auto">

       

       {/* SECOND ROW */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-8">

            {/* RECENT ORDERS */}
            <div className="lg:col-span-2 bg-white rounded-3xl shadow-md p-6">

              <div className="flex items-center justify-between mb-6">

                <h2 className="text-2xl font-bold text-gray-800">
                  Recent Orders
                </h2>

                <button className="text-[#0d4039] font-semibold">
                  View All
                </button>

              </div>

              <div className="space-y-4">

                <OrderRow
                  order="#ORD-1001"
                  customer="Ali Khan"
                  amount="Rs. 4,500"
                  status="Completed"
                />

                <OrderRow
                  order="#ORD-1002"
                  customer="Ahmed Raza"
                  amount="Rs. 2,800"
                  status="Pending"
                />

                <OrderRow
                  order="#ORD-1003"
                  customer="Danish Hafeez"
                  amount="Rs. 7,200"
                  status="Cooking"
                />

                <OrderRow
                  order="#ORD-1004"
                  customer="Usman"
                  amount="Rs. 1,950"
                  status="Delivered"
                />

              </div>

            </div>

            {/* QUICK ACTIONS */}
            <div className="bg-white rounded-3xl shadow-md p-6">

              <h2 className="text-2xl font-bold text-gray-800 mb-6">
                Quick Actions
              </h2>

              <div className="space-y-4">

                <QuickButton title="Create New Order" />

                <QuickButton title="Add Menu Item" />

                <QuickButton title="Reserve Table" />

                <QuickButton title="Add Customer" />

                <QuickButton title="Generate Report" />

              </div>

            </div>

          </div>

      </div>
      </div>
    </div>
  );
};

const StatCard = ({ title, value }) => {
  return (

    <div className="bg-white rounded-3xl shadow-md p-6">
      <h2 className="text-gray-500 text-sm">
        {title}
      </h2>

      <p className="text-4xl font-bold text-gray-800 mt-4">
        {value}
      </p>
    </div>

    
      
  );
};

const OrderRow = ({
  order,
  customer,
  amount,
  status,
}) => {

  return (
    <div className="flex items-center justify-between border border-gray-100 rounded-2xl p-4">

      <div>

        <h3 className="font-bold text-gray-800">
          {order}
        </h3>

        <p className="text-sm text-gray-500 mt-1">
          {customer}
        </p>

      </div>

      <div className="text-right">

        <p className="font-bold text-gray-800">
          {amount}
        </p>

        <span className="text-sm text-green-600 font-medium">
          {status}
        </span>

      </div>

    </div>
  );
};

const QuickButton = ({
  title,
}) => {

  return (
    <button className="w-full bg-[#0d4039] hover:bg-[#0a2d28] transition-all duration-300 text-white py-4 rounded-2xl font-semibold">

      {title}

    </button>
  );
};

export default Dashboard;