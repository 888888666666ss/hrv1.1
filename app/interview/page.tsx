import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Star, Video, Mic } from "lucide-react"

export default function VideoInterviewPage() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center gap-2">
          <div className="flex gap-1.5">
            <div className="w-3 h-3 rounded-full bg-red-500" />
            <div className="w-3 h-3 rounded-full bg-yellow-500" />
            <div className="w-3 h-3 rounded-full bg-green-500" />
          </div>
          <h1 className="text-lg font-medium text-gray-900 ml-4">视频面试、看简历、看测评、面试四合一</h1>
        </div>
      </header>

      <div className="p-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
          {/* Video Panels */}
          <Card className="bg-gray-100 p-4 flex flex-col items-center justify-center h-64 relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-br from-gray-200 to-gray-300" />
            <img
              src="/professional-asian-male-candidate.jpg"
              alt="候选人"
              className="w-32 h-40 object-cover rounded-lg mb-3 relative z-10"
            />
            <div className="flex items-center gap-2 relative z-10">
              <div className="w-2 h-2 rounded-full bg-teal-500" />
              <span className="text-sm font-medium text-gray-700">候选人</span>
            </div>
          </Card>

          <Card className="bg-gray-100 p-4 flex flex-col items-center justify-center h-64 relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-br from-gray-200 to-gray-300" />
            <img
              src="/professional-asian-female-business-manager.jpg"
              alt="业务经理"
              className="w-32 h-40 object-cover rounded-lg mb-3 relative z-10"
            />
            <div className="flex items-center gap-2 relative z-10">
              <div className="w-2 h-2 rounded-full bg-orange-500" />
              <span className="text-sm font-medium text-gray-700">业务经理</span>
            </div>
          </Card>

          <Card className="bg-gray-100 p-4 flex flex-col items-center justify-center h-64 relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-br from-gray-200 to-gray-300" />
            <img
              src="/professional-asian-female-hr-manager.jpg"
              alt="HR"
              className="w-32 h-40 object-cover rounded-lg mb-3 relative z-10"
            />
            <div className="flex items-center gap-2 relative z-10">
              <div className="w-2 h-2 rounded-full bg-teal-500" />
              <span className="text-sm font-medium text-gray-700">HR</span>
            </div>
          </Card>
        </div>

        {/* Control Buttons */}
        <div className="flex justify-center gap-4 mb-6">
          <Button size="lg" className="bg-teal-500 hover:bg-teal-600 text-white px-8">
            <Video className="w-5 h-5 mr-2" />
            视频
          </Button>
          <Button size="lg" className="bg-blue-500 hover:bg-blue-600 text-white px-8">
            <Mic className="w-5 h-5 mr-2" />
            音频
          </Button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left Panel - Assessment Report */}
          <div className="lg:col-span-2">
            <Tabs defaultValue="assessment" className="w-full">
              <TabsList className="grid w-full grid-cols-4 mb-4">
                <TabsTrigger value="resume">简历</TabsTrigger>
                <TabsTrigger value="assessment">测评</TabsTrigger>
                <TabsTrigger value="application">应聘</TabsTrigger>
                <TabsTrigger value="records">记录</TabsTrigger>
              </TabsList>

              <TabsContent value="assessment">
                <Card className="bg-gradient-to-br from-teal-400 to-teal-500 text-white p-6">
                  <h2 className="text-xl font-semibold mb-6 text-center">候选人测评报告</h2>

                  <div className="bg-white/10 backdrop-blur-sm rounded-lg p-6">
                    <div className="flex items-start gap-4 mb-6">
                      <img
                        src="/professional-asian-person-headshot.jpg"
                        alt="周晓晓"
                        className="w-20 h-20 rounded-lg object-cover"
                      />
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <span className="text-lg font-medium">周晓晓</span>
                          <span className="text-sm opacity-90">项目经理</span>
                        </div>
                        <div className="flex items-center gap-1 mb-3">
                          <span className="text-sm mr-2">产品经理胜任度模型</span>
                          {[1, 2, 3, 4].map((star) => (
                            <Star key={star} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                          ))}
                          <Star className="w-4 h-4 text-yellow-400" />
                        </div>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <div>
                        <div className="flex justify-between items-center mb-2">
                          <span className="text-sm">心理韧性</span>
                          <span className="text-sm font-medium">优秀潜质</span>
                        </div>
                        <div className="h-2 bg-white/20 rounded-full overflow-hidden">
                          <div className="h-full bg-white rounded-full" style={{ width: "75%" }} />
                        </div>
                      </div>

                      <div>
                        <div className="flex justify-between items-center mb-2">
                          <span className="text-sm">语言表达能力</span>
                          <span className="text-sm font-medium">团队发展潜能</span>
                        </div>
                        <div className="h-2 bg-white/20 rounded-full overflow-hidden">
                          <div className="h-full bg-white rounded-full" style={{ width: "60%" }} />
                        </div>
                      </div>
                    </div>
                  </div>
                </Card>
              </TabsContent>

              <TabsContent value="resume">
                <Card className="p-6">
                  <p className="text-gray-500 text-center py-12">简历内容</p>
                </Card>
              </TabsContent>

              <TabsContent value="application">
                <Card className="p-6">
                  <p className="text-gray-500 text-center py-12">应聘信息</p>
                </Card>
              </TabsContent>

              <TabsContent value="records">
                <Card className="p-6">
                  <p className="text-gray-500 text-center py-12">面试记录</p>
                </Card>
              </TabsContent>
            </Tabs>
          </div>

          {/* Right Panel - Interview Evaluation */}
          <div>
            <Card className="p-6">
              <h2 className="text-lg font-semibold mb-6 pb-3 border-b border-gray-200">面试评价表</h2>

              <div className="space-y-6">
                {/* Technical Level */}
                <div>
                  <h3 className="text-sm font-medium mb-3 flex items-center gap-2">
                    <div className="w-1 h-4 bg-teal-500 rounded" />
                    技术水平
                  </h3>
                  <RadioGroup defaultValue="good" className="space-y-2">
                    <div className="flex items-center space-x-2">
                      <RadioGroupItem value="excellent" id="excellent" />
                      <Label htmlFor="excellent" className="text-sm cursor-pointer">
                        优秀
                      </Label>
                    </div>
                    <div className="flex items-center space-x-2">
                      <RadioGroupItem value="good" id="good" />
                      <Label htmlFor="good" className="text-sm cursor-pointer">
                        良好
                      </Label>
                    </div>
                    <div className="flex items-center space-x-2">
                      <RadioGroupItem value="average" id="average" />
                      <Label htmlFor="average" className="text-sm cursor-pointer">
                        一般
                      </Label>
                    </div>
                  </RadioGroup>
                </div>

                {/* Interview Conclusion */}
                <div>
                  <h3 className="text-sm font-medium mb-3 flex items-center gap-2">
                    <div className="w-1 h-4 bg-teal-500 rounded" />
                    面试结论
                  </h3>
                  <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="flex-1 bg-transparent">
                      通过
                    </Button>
                    <Button variant="outline" size="sm" className="flex-1 bg-transparent">
                      待定
                    </Button>
                    <Button variant="outline" size="sm" className="flex-1 bg-transparent">
                      淘汰
                    </Button>
                  </div>
                </div>

                {/* Comments */}
                <div>
                  <Label htmlFor="comments" className="text-sm font-medium mb-2 block">
                    备注
                  </Label>
                  <Textarea id="comments" placeholder="请输入..." className="min-h-[100px] resize-none" />
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
