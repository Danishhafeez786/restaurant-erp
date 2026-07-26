import React from "react";

export default function FilterField({ label, children, className = "" }) {
  return (
    <div
      className={`
        flex flex-col md:flex-row
        rounded-xl border border-gray-300
        bg-white hover:bg-gray-100
        transition overflow-visible
        ${className}
      `}
    >
      {/* Label */}
      <div
        className="
    flex items-center
    px-3 py-2
    bg-gray-100
    border-b border-gray-200
    rounded-t-xl
    md:rounded-l-xl
    md:rounded-r-none
    md:border-b-0
    md:border-r
    md:min-w-[95px]
    md:h-12
    shrink-0
  "
      >
        <span className="text-sm font-semibold text-gray-800">{label}</span>
      </div>

      {/* Content */}
      <div
        className="
          flex items-center
          px-3 py-2
          md:py-0
          md:h-12
          flex-1
          relative
        "
      >
        {children}
      </div>
    </div>
  );
}
