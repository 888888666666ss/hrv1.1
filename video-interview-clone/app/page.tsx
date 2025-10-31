"use client"

import { useState } from "react"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Textarea } from "@/components/ui/textarea"
import { Star, Circle } from "lucide-react"

export default function VideoInterviewPage() {
  const [activeTab, setActiveTab] = useState("resume")

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b px-6 py-4">
        <div className="flex items-center gap-2">
          <div className="flex gap-1.5">
            <div className="w-3 h-3 rounded-full bg-red-500" />
            <div className="w-3 h-3 rounded-full bg-yellow-500" />
            <div className="w-3 h-3 rounded-full bg-green-500" />
          </div>
          <h1 className="text-lg font-medium ml-4">视频面试、看简历、看测评、面试四合一</h1>
        </div>
      </header>

      {/* Main Content */}
      <div className="p-6 grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Section - Video Panels and Assessment */}
        <div className="lg:col-span-2 space-y-6">
          {/* Video Grid */}
          <div className="grid grid-cols-3 gap-4">
            {/* Candidate Video */}
            <Card className="p-4 bg-gray-100 relative">
              <div className="aspect-[4/3] bg-gradient-to-br from-gray-200 to-gray-300 rounded-lg mb-3 flex items-center justify-center overflow-hidden">
                <img src="/professional-asian-male-candidate-smiling.jpg" alt="候选人" className="w-full h-full object-cover" />
              </div>
              <div className="flex items-center gap-2">
                <Circle className="w-2 h-2 fill-blue-500 text-blue-500" />
                <span className="text-sm font-medium">候选人</span>
              </div>
            </Card>

            {/* Business Manager Video */}
            <Card className="p-4 bg-gray-100 relative">
              <div className="aspect-[4/3] bg-gradient-to-br from-gray-200 to-gray-300 rounded-lg mb-3 flex items-center justify-center overflow-hidden">
                <img src="/professional-asian-female-business-manager.jpg" alt="业务经理" className="w-full h-full object-cover" />
              </div>
              <div className="flex items-center gap-2">
                <Circle className="w-2 h-2 fill-orange-500 text-orange-500" />
                <span className="text-sm font-medium">业务经理</span>
              </div>
            </Card>

            {/* HR Video */}
            <Card className="p-4 bg-gray-100 relative">
              <div className="aspect-[4/3] bg-gradient-to-br from-gray-200 to-gray-300 rounded-lg mb-3 flex items-center justify-center overflow-hidden">
                <img src="/professional-asian-female-hr-manager-smiling.jpg" alt="HR" className="w-full h-full object-cover" />
              </div>
              <div className="flex items-center gap-2">
                <Circle className="w-2 h-2 fill-cyan-500 text-cyan-500" />
                <span className="text-sm font-medium">HR</span>
              </div>
            </Card>
          </div>

          {/* Tabs Section */}
          <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
            <TabsList className="bg-white border-b w-full justify-start rounded-none h-auto p-0">
              <TabsTrigger
                value="resume"
                className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-cyan-500 px-6 py-3"
              >
                简历
              </TabsTrigger>
              <TabsTrigger
                value="assessment"
                className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-cyan-500 px-6 py-3"
              >
                测评
              </TabsTrigger>
              <TabsTrigger
                value="application"
                className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-cyan-500 px-6 py-3"
              >
                应聘
              </TabsTrigger>
              <TabsTrigger
                value="records"
                className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-cyan-500 px-6 py-3"
              >
                记录
              </TabsTrigger>
            </TabsList>

            <TabsContent value="assessment" className="mt-6">
              {/* Assessment Report Card */}
              <Card className="bg-gradient-to-r from-cyan-400 to-cyan-500 text-white p-6">
                <h2 className="text-xl font-semibold mb-6 text-center">候选人测评报告</h2>

                <div className="flex items-start gap-4 mb-6">
                  <div className="w-16 h-16 rounded-full bg-white/20 flex items-center justify-center overflow-hidden">
                    <img src="/professional-avatar.png" alt="候选人" className="w-full h-full object-cover" />
                  </div>
                  <div className="flex-1">
                    <div className="flex gap-4 mb-2">
                      <span className="px-3 py-1 bg-white/20 rounded text-sm">面试意愿</span>
                      <span className="px-3 py-1 bg-white/20 rounded text-sm">适应能力</span>
                    </div>
                    <div className="flex items-center gap-2 mt-3">
                      <span className="text-sm">产品经理胜任度模型</span>
                      {[1, 2, 3, 4].map((i) => (
                        <Star key={i} className="w-4 h-4 fill-white text-white" />
                      ))}
                      <Star className="w-4 h-4 text-white" />
                    </div>
                  </div>
                </div>

                <div className="space-y-4">
                  <div>
                    <div className="flex justify-between text-sm mb-2">
                      <span>心理韧性</span>
                      <span className="text-cyan-100">性格测评报告</span>
                    </div>
                    <div className="h-2 bg-white/20 rounded-full overflow-hidden">
                      <div className="h-full bg-white rounded-full" style={{ width: "25%" }} />
                    </div>
                  </div>

                  <div>
                    <div className="flex justify-between text-sm mb-2">
                      <span>认知能力</span>
                    </div>
                    <div className="h-2 bg-white/20 rounded-full overflow-hidden">
                      <div className="h-full bg-white rounded-full" style={{ width: "45%" }} />
                    </div>
                  </div>

                  <div>
                    <div className="flex justify-between text-sm mb-2">
                      <span>语言能力</span>
                      <span className="text-cyan-100">团队发展潜能报告</span>
                    </div>
                    <div className="h-2 bg-white/20 rounded-full overflow-hidden">
                      <div className="h-full bg-white rounded-full" style={{ width: "60%" }} />
                    </div>
                  </div>
                </div>

                <div className="flex gap-3 mt-6">
                  <Button variant="outline" className="flex-1 bg-white/10 border-white/30 text-white hover:bg-white/20">
                    查看报告
                  </Button>
                  <Button className="flex-1 bg-blue-600 hover:bg-blue-700 text-white border-0">查看报告</Button>
                </div>
              </Card>
            </TabsContent>

            <TabsContent value="resume">
              <Card className="p-6">
                <p className="text-muted-foreground">简历内容区域</p>
              </Card>
            </TabsContent>

            <TabsContent value="application">
              <Card className="p-6">
                <p className="text-muted-foreground">应聘信息区域</p>
              </Card>
            </TabsContent>

            <TabsContent value="records">
              <Card className="p-6">
                <p className="text-muted-foreground">面试记录区域</p>
              </Card>
            </TabsContent>
          </Tabs>
        </div>

        {/* Right Section - Evaluation Form */}
        <div className="space-y-6">
          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-6">面试评价表</h2>

            {/* Technical Level Section */}
            <div className="mb-6">
              <h3 className="text-sm font-medium mb-4 pb-2 border-b-2 border-cyan-500">技术水平</h3>
              <div className="space-y-3">
                <div className="flex items-center gap-2">
                  <input type="checkbox" id="excellent" className="rounded" />
                  <label htmlFor="excellent" className="text-sm">
                    优秀
                  </label>
                </div>
                <div className="flex items-center gap-2">
                  <input type="checkbox" id="good" className="rounded" />
                  <label htmlFor="good" className="text-sm">
                    良好
                  </label>
                </div>
                <div className="flex items-center gap-2">
                  <input type="checkbox" id="average" className="rounded" />
                  <label htmlFor="average" className="text-sm">
                    一般
                  </label>
                </div>
              </div>
            </div>

            {/* Interview Conclusion Section */}
            <div>
              <h3 className="text-sm font-medium mb-4 pb-2 border-b-2 border-cyan-500">面试结论</h3>
              <div className="flex gap-2 mb-4">
                <Button variant="outline" size="sm" className="text-xs bg-transparent">
                  通过
                </Button>
                <Button variant="outline" size="sm" className="text-xs bg-transparent">
                  待定
                </Button>
                <Button variant="outline" size="sm" className="text-xs bg-transparent">
                  淘汰
                </Button>
              </div>
              <Textarea placeholder="填写内容" className="min-h-[100px] resize-none" />
            </div>
          </Card>
        </div>
      </div>
    </div>
  )
}
