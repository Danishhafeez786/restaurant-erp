export default function SummaryCards({ summary }) {

    const cards = [

        {
            title: "Roles",
            value: summary?.roles ?? 0,
            color: "text-blue-600"
        },

        {
            title: "Permissions",
            value: summary?.permissions ?? 0,
            color: "text-green-600"
        },

        {
            title: "Assigned",
            value: summary?.assigned ?? 0,
            color: "text-purple-600"
        },

        {
            title: "Active",
            value: summary?.active ?? 0,
            color: "text-orange-600"
        }

    ];
    
    return (
        
        <div className="grid grid-cols-2 xl:grid-cols-4 gap-5">

            {

                cards.map((card) => (

                    <div
                        key={card.title}
                        className="bg-white rounded-2xl border border-gray-200 shadow-sm hover:shadow-lg transition-all duration-300 p-6"
                    >

                        <p className="text-sm text-gray-500">

                            {card.title}

                        </p>

                        <h2 className={`mt-3 text-4xl font-bold ${card.color}`}>

                            {card.value}

                        </h2>

                    </div>

                ))

            }

        </div>

    );

}