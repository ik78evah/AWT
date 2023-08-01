import React from "react";

export default function TableHeader({ title }) {
   return (
      <th scope="col"
          className="px-6 py-3 text-xs font-medium tracking-wider text-left text-gray-700 uppercase">
         {title}
      </th>
   )
}