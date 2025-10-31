import { FileText, Calendar, RefreshCw, Shield, Eye } from "lucide-react"
import { Card } from "@/components/ui/card"

export default function AssessmentIntegrationPage() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center gap-3">
          <div className="flex gap-1.5">
            <div className="w-3 h-3 rounded-full bg-red-500" />
            <div className="w-3 h-3 rounded-full bg-yellow-500" />
            <div className="w-3 h-3 rounded-full bg-green-500" />
          </div>
          <h1 className="text-lg font-medium text-gray-900">招聘测评一体化</h1>
        </div>
      </header>

      {/* Main Content */}
      <main className="p-8">
        <div className="max-w-7xl mx-auto">
          {/* Top Section with Profile and Action Cards */}
          <div className="flex items-start gap-8 mb-12">
            {/* Profile Card */}
            <Card className="relative p-6 bg-white shadow-sm">
              <div className="absolute -top-3 -left-3">
                <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-teal-400 text-white">
                  团队合作
                </span>
              </div>
              <div className="relative">
                <img
                  src="/professional-asian-person-headshot.jpg"
                  alt="候选人照片"
                  className="w-32 h-40 object-cover rounded-lg"
                />
                <span className="absolute bottom-2 right-2 inline-flex items-center px-2 py-1 rounded text-xs font-medium bg-blue-500 text-white">
                  职业反馈
                </span>
              </div>
              <p className="mt-3 text-center text-sm font-medium text-gray-700">北小森</p>
            </Card>

            {/* Spacer */}
            <div className="flex-1" />

            {/* Action Cards */}
            <div className="flex gap-4">
              <Card className="p-6 bg-white shadow-sm hover:shadow-md transition-shadow cursor-pointer w-36">
                <div className="flex flex-col items-center gap-3">
                  <div className="w-12 h-12 rounded-lg bg-blue-50 flex items-center justify-center">
                    <FileText className="w-6 h-6 text-blue-500" />
                  </div>
                  <span className="text-sm font-medium text-gray-700 text-center">构建人才标准</span>
                </div>
              </Card>

              <Card className="p-6 bg-white shadow-sm hover:shadow-md transition-shadow cursor-pointer w-36">
                <div className="flex flex-col items-center gap-3">
                  <div className="w-12 h-12 rounded-lg bg-teal-50 flex items-center justify-center">
                    <Calendar className="w-6 h-6 text-teal-500" />
                  </div>
                  <span className="text-sm font-medium text-gray-700 text-center">邀定测评活动</span>
                </div>
              </Card>

              <Card className="p-6 bg-white shadow-sm hover:shadow-md transition-shadow cursor-pointer w-36">
                <div className="flex flex-col items-center gap-3">
                  <div className="w-12 h-12 rounded-lg bg-teal-50 flex items-center justify-center">
                    <RefreshCw className="w-6 h-6 text-teal-500" />
                  </div>
                  <span className="text-sm font-medium text-gray-700 text-center">自动发起测评</span>
                </div>
              </Card>
            </div>
          </div>

          {/* Workflow Diagram */}
          <div className="relative">
            {/* Connecting Lines */}
            <svg className="absolute inset-0 w-full h-full pointer-events-none" style={{ zIndex: 0 }}>
              {/* Line from Data Analysis to Report */}
              <line x1="120" y1="80" x2="400" y2="200" stroke="#e5e7eb" strokeWidth="2" strokeDasharray="5,5" />
              {/* Line from Optimization to Report */}
              <line x1="120" y1="180" x2="400" y2="200" stroke="#e5e7eb" strokeWidth="2" strokeDasharray="5,5" />
              {/* Line from Quality Analysis to Report */}
              <line x1="120" y1="320" x2="400" y2="250" stroke="#e5e7eb" strokeWidth="2" strokeDasharray="5,5" />
              {/* Line from Report to Progress */}
              <line x1="650" y1="225" x2="850" y2="225" stroke="#e5e7eb" strokeWidth="2" strokeDasharray="5,5" />
            </svg>

            <div className="relative grid grid-cols-3 gap-8" style={{ zIndex: 1 }}>
              {/* Left Column - Process Steps */}
              <div className="flex flex-col gap-6">
                <Card className="p-4 bg-white shadow-sm">
                  <p className="text-sm font-medium text-gray-700">数据分析</p>
                </Card>
                <Card className="p-4 bg-white shadow-sm">
                  <p className="text-sm font-medium text-gray-700">精准优化</p>
                </Card>
                <Card className="p-6 bg-white shadow-sm">
                  <div className="flex flex-col items-center gap-3">
                    <div className="w-12 h-12 rounded-lg bg-teal-50 flex items-center justify-center">
                      <Shield className="w-6 h-6 text-teal-500" />
                    </div>
                    <span className="text-sm font-medium text-gray-700 text-center">人才质量分析</span>
                  </div>
                </Card>
              </div>

              {/* Center Column - Assessment Report */}
              <div className="flex items-center">
                <Card className="p-6 bg-white shadow-sm w-full">
                  <h3 className="text-sm font-medium text-gray-500 mb-2">查看测评报告</h3>
                  <h2 className="text-xl font-semibold text-gray-900 mb-4">北小森</h2>
                  <div className="space-y-3">
                    <div>
                      <div className="flex items-center justify-between mb-2">
                        <span className="text-sm text-gray-600">岗位匹配度已配置：</span>
                        <span className="text-sm font-medium text-blue-600">较高</span>
                      </div>
                      <div className="space-y-2">
                        <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                          <div
                            className="h-full bg-gradient-to-r from-teal-400 to-blue-500 rounded-full"
                            style={{ width: "85%" }}
                          />
                        </div>
                        <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                          <div
                            className="h-full bg-gradient-to-r from-teal-400 to-blue-500 rounded-full"
                            style={{ width: "92%" }}
                          />
                        </div>
                        <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                          <div
                            className="h-full bg-gradient-to-r from-teal-400 to-blue-500 rounded-full"
                            style={{ width: "78%" }}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </Card>
              </div>

              {/* Right Column - Progress Tracking */}
              <div className="flex items-center">
                <Card className="p-6 bg-white shadow-sm w-full">
                  <div className="flex flex-col items-center gap-3">
                    <div className="w-12 h-12 rounded-lg bg-teal-50 flex items-center justify-center">
                      <Eye className="w-6 h-6 text-teal-500" />
                    </div>
                    <span className="text-sm font-medium text-gray-700 text-center">选拔测评进度</span>
                  </div>
                </Card>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
