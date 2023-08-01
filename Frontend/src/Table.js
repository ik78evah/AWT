import TableHeader from "./TableHeader";
import AnalyticsRow from "./AnalyticsRow";
import React from "react";

export default function Table({ headers, rows}) {
   return (
      <div className="flex flex-col px-5">
         <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
            <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
               <div className="overflow-hidden shadow-md sm:rounded-lg">
                  <table className="table-auto min-w-full">
                     <thead className="bg-gray-200">
                     <tr>
                        {headers.map(name =>
                           <TableHeader key={name} title={name}/>
                        )}
                     </tr>
                     </thead>
                     <tbody className={"bg-white"}>
                     {rows.map(row =>
                        <AnalyticsRow key={row.identifier} item={row}/>
                     )}
                     </tbody>
                  </table>
               </div>
            </div>
         </div>
      </div>
   )
}