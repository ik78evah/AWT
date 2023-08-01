import React from "react";

export default function DetailView({ title, value }) {
   return (
      <tr className="flex items-center place-content-between bg-white border-b hover:bg-gray-50 min-w-fit">
         <td className="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap">
            {title}
         </td>
         <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap text-left">
            {value}
         </td>
      </tr>
   )
}